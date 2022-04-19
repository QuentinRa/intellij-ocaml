package com.ocaml.ide.highlight.intentions;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.PsiFile;
import com.ocaml.sdk.output.CompilerOutputMessage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;
import java.util.Set;

public interface CompilerOutputProvider {

    /**
     * @param file              the psiFile associated with the current version of the file
     * @param editor            the editor
     * @param homePath          the path to the SDK home
     * @param moduleRootManager can be used to determine another output folder, or to get the sources' folders.
     * @param outputFolder      the default value for the output folder
     * @return Collected information that will be used to compile and generate annotations
     * using {@link #doAnnotate}.
     */
    CollectedInfo collectInformation(@NotNull PsiFile file, @NotNull Editor editor,
                                     String homePath, @NotNull ModuleRootManager moduleRootManager,
                                     String outputFolder);

    /**
     * Compile the files
     * @param collectedInfo the collected information
     * @param log           can be used to log warnings/errors
     * @return the annotation results
     */
    ExternalCompilerResult doAnnotate(CollectedInfo collectedInfo, Logger log);

    class CollectedInfo {
        @NotNull public final CompilerOutputProvider myAnnotator;
        @NotNull public final PsiFile mySourcePsiFile; // original file
        @NotNull public final Editor myEditor;
        @NotNull public final String myHomePath;
        @NotNull public final String myTargetFile; // path to the file, relative
        @Nullable public final PsiFile myTargetMli;
        @NotNull public final Set<Pair<String, PsiFile>> myDependencies;
        @NotNull public final String myOutputFolder;

        public CollectedInfo(@NotNull CompilerOutputProvider annotator, @NotNull PsiFile sourcePsiFile,
                             @NotNull Editor editor, @NotNull String homePath,
                             @NotNull String targetFile, @Nullable PsiFile mli,
                             @NotNull Set<Pair<String, PsiFile>> deps, @NotNull String outputFolder) {
            myAnnotator = annotator;
            mySourcePsiFile = sourcePsiFile;
            myEditor = editor;
            myHomePath = homePath;
            myTargetFile = targetFile;
            myTargetMli = mli;
            myDependencies = deps;
            myOutputFolder = outputFolder;
        }
    }

    class ExternalCompilerResult {
        public final List<CompilerOutputMessage> myOutputInfo;
        public final Editor myEditor;
        public final File myAnnotFile;

        public ExternalCompilerResult(List<CompilerOutputMessage> outputInfo, Editor editor, File annotFile) {
            myOutputInfo = outputInfo;
            myEditor = editor;
            myAnnotFile = annotFile;
        }
    }
}
