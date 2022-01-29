package com.ocaml.ide.highlight.annotations;

import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessHandlerFactory;
import com.intellij.lang.annotation.*;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.impl.TextRangeInterval;
import com.intellij.openapi.module.*;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.problems.Problem;
import com.intellij.problems.WolfTheProblemSolver;
import com.intellij.psi.PsiFile;
import com.intellij.xml.util.XmlStringUtil;
import com.ocaml.sdk.OCamlSdkType;
import com.ocaml.sdk.output.CompilerOutputMessage;
import com.ocaml.sdk.output.CompilerOutputParser;
import com.ocaml.sdk.providers.OCamlSdkProvidersManager;
import com.ocaml.utils.files.OCamlTempDirUtils;
import com.ocaml.utils.logs.OCamlLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;

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
        Sdk sdk = ModuleRootManager.getInstance(module).getSdk();
        if (sdk == null || !(sdk.getSdkType() instanceof OCamlSdkType)) return null;
        // get home path
        String homePath = sdk.getHomePath();
        if (homePath == null) return null;

        LOG.trace("CollectInformation OK");

        return new CollectedInfo(file, editor, homePath);
    }

    /*
     * we need to make a copy with the REAL content of the file, and work on this copy.
     * if we are working on the given file, we are not compiling/... the "latest" version
     * as some changes may not have been committed.
     */

    @Override public @Nullable AnnotationResult doAnnotate(@NotNull CollectedInfo collectedInfo) {
        Project project = collectedInfo.mySourcePsiFile.getProject();
        VirtualFile sourceFile = collectedInfo.mySourcePsiFile.getVirtualFile();

        // Create and clean temporary compilation directory
        String nameWithoutExtension = sourceFile.getNameWithoutExtension();
        File tempCompilationDirectory = OCamlTempDirUtils.getOrCreateTempDirectory(project, LOG);
        // Annotator functions are called asynchronously and can be interrupted,
        // leaving files on disk if operation is aborted.
        // -> Clean current temp directory, but for the current file only:
        // avoid erasing files when concurrent compilation happens.
        OCamlTempDirUtils.cleanTempDirectory(tempCompilationDirectory, nameWithoutExtension);

        // Creates a temporary file on disk with a copy of the current document.
        File sourceTempFile = OCamlTempDirUtils.copyToTempFile(tempCompilationDirectory,
                collectedInfo.mySourcePsiFile, nameWithoutExtension, LOG);
        if (sourceTempFile == null)  return null;

        File cmtFile = new File(tempCompilationDirectory, nameWithoutExtension + ".cmt");
        try {
            // get compiler
            GeneralCommandLine cli = OCamlSdkProvidersManager.INSTANCE.getCompilerAnnotatorCommand(
                    collectedInfo.myHomePath,
                    sourceTempFile.getPath(),
                    tempCompilationDirectory.getAbsolutePath(),
                    sourceFile.getNameWithoutExtension()
            );
            if (cli == null) {
                LOG.error("No cli found for "+collectedInfo.myHomePath+".");
                return null;
            }
            cli.setRedirectErrorStream(true);

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
            }
            // done
            outputParser.inputDone();

            // cleaning
            FileUtil.delete(sourceTempFile);
            // File baseFile = new File(sourceTempFile.getParent(), FileUtil.getNameWithoutExtension(sourceTempFile));
            // FileUtil.delete(new File(baseFile.getPath() + ".cmo"));
            // FileUtil.delete(new File(baseFile.getPath() + ".cmi"));
            return new AnnotationResult(info, collectedInfo.myEditor, cmtFile);
        } catch (Exception e) {
            LOG.error("Error while processing annotations", e);
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
            builder = builder.range(new TextRangeInterval(startOffset, endOffset));
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
