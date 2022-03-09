package com.ocaml.ide.insight.annotations.providers;

import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.lang.annotation.AnnotationBuilder;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.ExternalAnnotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.impl.TextRangeInterval;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.CompilerModuleExtension;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.pointers.VirtualFilePointer;
import com.intellij.problems.Problem;
import com.intellij.problems.WolfTheProblemSolver;
import com.intellij.psi.PsiFile;
import com.ocaml.ide.insight.OCamlAnnotResultsService;
import com.ocaml.ide.insight.annotations.OCamlAnnotation;
import com.ocaml.ide.insight.annotations.OCamlMessageAdaptor;
import com.ocaml.sdk.OCamlSdkType;
import com.ocaml.sdk.output.CompilerOutputMessage;
import com.ocaml.utils.logs.OCamlLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 * Relies on the ocaml compiler (ocamlc) to provide warnings, errors, and alerts
 * in real-time, in the editor.
 */
public class CompilerOutputAnnotator extends ExternalAnnotator<CollectedInfo, AnnotationResult> implements DumbAware {

    private static final Logger LOG = OCamlLogger.getSdkInstance("annotator");
    private static final String TEMP_COMPILATION_FOLDER = "/tmp/";

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

        // output folder
        CompilerModuleExtension compilerModuleExtension = moduleRootManager.getModuleExtension(CompilerModuleExtension.class);
        if (compilerModuleExtension == null) return null; // was null in CLion
        VirtualFilePointer outputPointer = compilerModuleExtension.getCompilerOutputPointer();
        String outputFolder = outputPointer.getPresentableUrl() + TEMP_COMPILATION_FOLDER;

        LOG.trace("Working on file:" + sourceFile.getPath());

        return findCollector(file, editor, homePath, moduleRootManager, outputFolder);
    }

    private CollectedInfo findCollector(PsiFile file, Editor editor, String homePath, ModuleRootManager moduleRootManager, String outputFolder) {
        return new BasicExternalAnnotator().collectInformation(file, editor, homePath, moduleRootManager, outputFolder);
    }

    @Override public @Nullable AnnotationResult doAnnotate(@NotNull CollectedInfo collectedInfo) {
        return collectedInfo.myAnnotator.doAnnotate(collectedInfo, LOG);
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
                AnnotationBuilder builder = holder.newAnnotation(t, "<html>" + message.header.replace("\n", "<br/>") + "</html>");
                builder = builder.fileLevel();
                builder = builder.tooltip(message.content);
                builder.create();
            }

            if (message.normalLevel) {
                AnnotationBuilder builder = holder.newAnnotation(t, message.header);
                if (!message.fileLevel) builder = range == null ? builder.afterEndOfLine() : builder.range(range);
                builder = builder.tooltip(message.content);
                builder = message.hasCustomHighLightType() ? builder.highlightType(message.highlightType) : builder;
                for (IntentionAction fix : message.fixes) {
                    builder = builder.withFix(fix); // fix
                }
                builder.create();
            }

            if (message.isError()) {
                problems.add(wolfTheProblemSolver.convertToProblem(
                        virtualFile, message.startLine, message.startColumn,
                        message.content.split("\n")
                ));
            }
        }

        OCamlAnnotResultsService annotResultsService = project.getService(OCamlAnnotResultsService.class);
        if (!problems.isEmpty()) {
            wolfTheProblemSolver.reportProblems(virtualFile, problems);
            annotResultsService.clearForFile(virtualFile.getPath());
        } else {
            wolfTheProblemSolver.clearProblems(virtualFile);
            annotResultsService.updateForFile(virtualFile.getPath(), annotationResult.myAnnotFile);
        }
    }
}
