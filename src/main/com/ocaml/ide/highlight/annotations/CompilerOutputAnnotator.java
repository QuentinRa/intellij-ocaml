package com.ocaml.ide.highlight.annotations;

import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessHandlerFactory;
import com.intellij.lang.annotation.*;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.impl.TextRangeInterval;
import com.intellij.openapi.module.*;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.*;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.pointers.VirtualFilePointer;
import com.intellij.problems.Problem;
import com.intellij.problems.WolfTheProblemSolver;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.ocaml.ide.files.OCamlFileType;
import com.ocaml.ide.files.OCamlInterfaceFileType;
import com.ocaml.sdk.OCamlSdkType;
import com.ocaml.sdk.output.CompilerOutputMessage;
import com.ocaml.sdk.output.CompilerOutputParser;
import com.ocaml.sdk.providers.OCamlSdkProvidersManager;
import com.ocaml.sdk.providers.utils.CompileWithCmtInfo;
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
        // FIX: Read access is allowed from inside read-action (or EDT) only (see com.intellij.openapi.application.Application.runReadAction())
        //noinspection RedundantSuppression
        @SuppressWarnings("deprecation")
        File sourceTempFile = ApplicationManager.getApplication().runReadAction( (Computable<File>)
                () -> OCamlFileUtils.copyToTempFile(targetFolder, collectedInfo.mySourcePsiFile, sourceFile.getName(), LOG)
        );
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
                CompileWithCmtInfo compiler = OCamlSdkProvidersManager.INSTANCE.getCompileCommandWithCmt(
                        collectedInfo.myHomePath,
                        myOutputFolder.getAbsolutePath(),
                        interfaceTempFile.get().getPath(),
                        targetFolder.getAbsolutePath(),
                        sourceFile.getNameWithoutExtension()
                );
                if (compiler == null) {
                    LOG.error("No cli found for "+collectedInfo.myHomePath+" (mli).");
                    return null;
                }
                Process process = compiler.cli.createProcess();
                process.waitFor(); // wait
            }

            // get compiler
            CompileWithCmtInfo compiler = OCamlSdkProvidersManager.INSTANCE.getCompileCommandWithCmt(
                    collectedInfo.myHomePath,
                    myOutputFolder.getAbsolutePath(),
                    sourceTempFile.getPath(),
                    targetFolder.getAbsolutePath(),
                    sourceFile.getNameWithoutExtension()
            );
            if (compiler == null) {
                LOG.error("No cli found for "+collectedInfo.myHomePath+" (ml).");
                return null;
            }

            OSProcessHandler processHandler = ProcessHandlerFactory
                    .getInstance()
                    .createColoredProcessHandler(compiler.cli);
            Process process = processHandler.getProcess();
            processHandler.startNotify();

            ArrayList<CompilerOutputMessage> info = new ArrayList<>();
            CompilerOutputParser outputParser = new CompilerOutputParser() {
                @Override protected void onMessageReady(@NotNull CompilerOutputMessage message) {
                    message.content = OCamlMessageAdaptor.temperPaths(message.content, compiler.rootFolderForTempering);
                    LOG.debug("added:" + message.header() + " (line->" + message.filePosition.getStartLine() + ")");
                    info.add(message);
                }
            };
            try (BufferedReader stdin = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = stdin.readLine()) != null) {
                    outputParser.parseLine(line);
                }
                // done
                outputParser.inputDone();
            } catch (IOException e){
                // may occur if the file was removed, because it will be compiled again?
                LOG.warn("Reading '"+sourceFile.getName()+"' failed ("+e.getMessage()+").");
                return null;
            } finally {
                // delete
                Files.deleteIfExists(sourceTempFile.toPath());
                if (interfaceTempFile.get() != null) Files.deleteIfExists(interfaceTempFile.get().toPath());
            }

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

        for (CompilerOutputMessage m : annotationResult.myOutputInfo) {
            OCamlAnnotation message = OCamlMessageAdaptor.temper(m, file, editor);

            // type
            HighlightSeverity t;
            if (message.isWarning()) t = HighlightSeverity.WARNING;
            else if (message.isError()) t = HighlightSeverity.ERROR;
            else if (message.isAlert()) t = HighlightSeverity.WEAK_WARNING;
            else t = HighlightSeverity.INFORMATION;

            TextRangeInterval range = message.computePosition();

            // create
            if (message.fileLevel) {
                // create fileLevel
                AnnotationBuilder builder = holder.newAnnotation(t, "<html>"+message.header.replace("\n", "<br/>")+"</html>");
                builder = builder.fileLevel();
                builder = builder.tooltip(message.content);
                builder.create();
            }

            AnnotationBuilder builder = holder.newAnnotation(t, message.header);
            if (!message.fileLevel) builder = range == null ? builder.afterEndOfLine() : builder.range(range);
            builder = builder.tooltip(message.content);
            builder = message.hasCustomHighLightType() ? builder.highlightType(message.highlightType) : builder;
            for (IntentionAction fix: message.fixes) {
                builder = builder.withFix(fix); // fix
            }
            builder.create();

            if (message.isError()) {
                problems.add(wolfTheProblemSolver.convertToProblem(
                        virtualFile, message.startLine, message.startColumn,
                        message.content.split("\n")
                ));
            }
        }

        if (!problems.isEmpty()) wolfTheProblemSolver.reportProblems(virtualFile, problems);
        else wolfTheProblemSolver.clearProblems(virtualFile);
    }
}
