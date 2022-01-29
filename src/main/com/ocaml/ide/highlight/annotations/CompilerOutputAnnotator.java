package com.ocaml.ide.highlight.annotations;

import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessHandlerFactory;
import com.intellij.lang.annotation.*;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.impl.TextRangeInterval;
import com.intellij.openapi.module.*;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.*;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.pointers.VirtualFilePointer;
import com.intellij.problems.Problem;
import com.intellij.problems.WolfTheProblemSolver;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.xml.util.XmlStringUtil;
import com.ocaml.ide.files.OCamlFileType;
import com.ocaml.ide.files.OCamlInterfaceFileType;
import com.ocaml.sdk.OCamlSdkType;
import com.ocaml.sdk.output.CompilerOutputMessage;
import com.ocaml.sdk.output.CompilerOutputParser;
import com.ocaml.sdk.providers.OCamlSdkProvidersManager;
import com.ocaml.utils.files.OCamlFileUtils;
import com.ocaml.utils.logs.OCamlLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Relies on the ocaml compiler (ocamlc) to provide warnings, errors, and alerts
 * in real-time, in the editor.
 */
public class CompilerOutputAnnotator extends ExternalAnnotator<CollectedInfo, AnnotationResult> implements DumbAware {

    private static final Logger LOG = OCamlLogger.getSdkInstance("annotator");

    /* ensure that we got an OCaml SDK */

    @Override
    public @Nullable CollectedInfo collectInformation(@NotNull PsiFile file, @NotNull Editor editor,
                                                      boolean hasErrors) {
        Project project = file.getProject();
        VirtualFile sourceFile = file.getVirtualFile();

        // not inside a module?
        Module module = ModuleUtil.findModuleForFile(sourceFile, project);
        if (module == null) return null;
        // find sdk
        ModuleRootManager moduleRootManager = ModuleRootManager.getInstance(module);
        Sdk sdk = moduleRootManager.getSdk();
        if (sdk == null || !(sdk.getSdkType() instanceof OCamlSdkType)) return null;
        // get home path
        String homePath = sdk.getHomePath();
        if (homePath == null) return null;

        String targetFile = null;

        LOG.trace("Working on file:"+sourceFile.getPath());

        // find the file we are trying to compile
        for (VirtualFile root : moduleRootManager.getSourceRoots()) {
            boolean under = VfsUtil.isUnder(sourceFile, Set.of(root));
            if (!under) continue;
            // we are asking for the parent, because we want the root inside the relative path
            // ex: src/errors/mismatch/missing_impl/file.ml
            targetFile = VfsUtil.getRelativePath(sourceFile, root);
            break;
        }
        if (targetFile == null) return null;

        // look for .mli
        PsiFile mli = null;
        if (OCamlFileType.isFile(sourceFile.getName())) {
            VirtualFile mliV = sourceFile.getParent().findChild(OCamlInterfaceFileType.fromSource(sourceFile.getName()));
            if (mliV != null) mli = PsiManager.getInstance(project).findFile(mliV);
        }

        // output folder
        CompilerModuleExtension compilerModuleExtension = moduleRootManager.getModuleExtension(CompilerModuleExtension.class);
        VirtualFilePointer outputPointer = compilerModuleExtension.getCompilerOutputPointer();
        String outputFolder = outputPointer.getPresentableUrl() + "/tmp/";
        return new CollectedInfo(file, editor, homePath, targetFile, mli, outputFolder);
    }

    @Override public @Nullable AnnotationResult doAnnotate(@NotNull CollectedInfo collectedInfo) {
        // build/out folder
        File myOutputFolder = new File(collectedInfo.myOutputFolder);
        if (!myOutputFolder.exists() && !myOutputFolder.mkdirs()) {
            LOG.warn("Couldn't create '"+myOutputFolder+"'");
            return null;
        }
        // target folder
        File targetFolder = new File(myOutputFolder, collectedInfo.myTargetFile).getParentFile();
        if (!targetFolder.exists() && !targetFolder.mkdirs()) {
            LOG.warn("Couldn't create '"+targetFolder+"'");
            return null;
        }

        VirtualFile sourceFile = collectedInfo.mySourcePsiFile.getVirtualFile();

        // Creates a temporary file on disk with a copy of the current document.
        // We need to make a copy with the REAL content of the file, and work on this copy.
        // If we are working on the given file, we are not compiling/... the "latest" version,
        // as some changes may not have been committed.
        File sourceTempFile = OCamlFileUtils.copyToTempFile(targetFolder, collectedInfo.mySourcePsiFile, sourceFile.getName(), LOG);
        if (sourceTempFile == null)  return null;

        AtomicReference<File> interfaceTempFile = new AtomicReference<>();
        // only if we are compiling a .ml,
        // and we got a .mli
        if (collectedInfo.myTargetMli != null) {
            ApplicationManager.getApplication().runReadAction(
                    () -> interfaceTempFile.set(OCamlFileUtils.copyToTempFile(targetFolder, collectedInfo.myTargetMli, collectedInfo.myTargetMli.getName(), LOG))
            );
        }

        String nameWithoutExtension = sourceFile.getNameWithoutExtension();
        File cmtFile = new File(targetFolder, nameWithoutExtension + ".cmt");
        try {
            // compile .mli
            if (interfaceTempFile.get() != null) {
                // get compiler
                GeneralCommandLine cli = OCamlSdkProvidersManager.INSTANCE.getCompilerAnnotatorCommand(
                        collectedInfo.myHomePath,
                        interfaceTempFile.get().getPath(),
                        targetFolder.getAbsolutePath(),
                        sourceFile.getNameWithoutExtension()
                );
                if (cli == null) {
                    LOG.error("No cli found for "+collectedInfo.myHomePath+" (mli).");
                    return null;
                }
                Process process = cli.createProcess();
                process.waitFor(); // wait
            }

            // get compiler
            GeneralCommandLine cli = OCamlSdkProvidersManager.INSTANCE.getCompilerAnnotatorCommand(
                    collectedInfo.myHomePath,
                    sourceTempFile.getPath(),
                    targetFolder.getAbsolutePath(),
                    sourceFile.getNameWithoutExtension()
            );
            if (cli == null) {
                LOG.error("No cli found for "+collectedInfo.myHomePath+" (ml).");
                return null;
            }

            OSProcessHandler processHandler = ProcessHandlerFactory
                    .getInstance()
                    .createColoredProcessHandler(cli);
            Process process = processHandler.getProcess();
            processHandler.startNotify();

            ArrayList<CompilerOutputMessage> info = new ArrayList<>();
            CompilerOutputParser outputParser = new CompilerOutputParser() {
                @Override protected void onMessageReady(@NotNull CompilerOutputMessage message) {
                    LOG.debug("added:" + message.header() + " (line->" + message.filePosition.getStartLine() + ")");
                    info.add(message);
                }
            };
            try (BufferedReader stdin = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = stdin.readLine()) != null) {
                    outputParser.parseLine(line);
                }
            } catch (IOException e){
                LOG.warn("reading "+nameWithoutExtension+" error. Messages are "+info, e);
            }
            // done
            outputParser.inputDone();

            // delete
            Files.deleteIfExists(sourceTempFile.toPath());
            if (interfaceTempFile.get() != null) Files.deleteIfExists(interfaceTempFile.get().toPath());

            return new AnnotationResult(info, collectedInfo.myEditor, cmtFile);
        } catch (Exception e) {
            LOG.error("Error while processing annotations", e);
            // delete
            try {
                Files.deleteIfExists(sourceTempFile.toPath());
                if (interfaceTempFile.get() != null) Files.deleteIfExists(interfaceTempFile.get().toPath());
            } catch (IOException ex) {
                LOG.error("Files were not deleted", ex);
            }
            return null;
        }
    }

    @Override
    public void apply(@NotNull PsiFile file, @NotNull AnnotationResult annotationResult,
                      @NotNull AnnotationHolder holder) {
        VirtualFile virtualFile = file.getVirtualFile();
        Project project = file.getProject();
        Editor editor = annotationResult.myEditor;

        WolfTheProblemSolver wolfTheProblemSolver = WolfTheProblemSolver.getInstance(project);
        ArrayList<Problem> problems = new ArrayList<>();

        for (CompilerOutputMessage message : annotationResult.myOutputInfo) {
            // type
            HighlightSeverity t;
            if (message.isWarning()) t = HighlightSeverity.WARNING;
            else if (message.isError()) t = HighlightSeverity.ERROR;
            else if (message.isAlert()) t = HighlightSeverity.WEAK_WARNING;
            else t = HighlightSeverity.INFORMATION;

            // position
            int colStart = message.filePosition.getStartColumn();
            int colEnd = message.filePosition.getEndColumn();
            int lineStart = message.filePosition.getStartLine() - 1;
            int lineEnd = message.filePosition.getEndLine() - 1;

            if (colStart == -1) colStart = 0;
            if (colEnd == -1) colEnd = 1;

            LogicalPosition start = new LogicalPosition(lineStart, colStart);
            LogicalPosition end = new LogicalPosition(lineEnd, colEnd);
            int startOffset = editor.isDisposed() ? 0 : editor.logicalPositionToOffset(start);
            int endOffset = editor.isDisposed() ? 0 : editor.logicalPositionToOffset(end);

            // create
            AnnotationBuilder builder = holder.newAnnotation(t, message.content);
            if (startOffset != endOffset) builder = builder.range(new TextRangeInterval(startOffset, endOffset));
            else builder = builder.afterEndOfLine(); // otherwise, it does not make any sense
            builder = builder.tooltip(XmlStringUtil.wrapInHtml(message.content.replace("\n", "<br/>")));
            // builder = builder.withFix(null); // fix
            builder.create();

            if (message.isError()) {
                problems.add(wolfTheProblemSolver.convertToProblem(
                        virtualFile, lineStart, colStart,
                        message.content.split("\n")
                ));
            }
        }

        if (!problems.isEmpty())
            wolfTheProblemSolver.reportProblems(virtualFile, problems);
        else
            wolfTheProblemSolver.clearProblems(virtualFile);
    }
}
