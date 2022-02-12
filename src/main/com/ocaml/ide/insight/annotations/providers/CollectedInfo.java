package com.ocaml.ide.insight.annotations.providers;

import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class CollectedInfo {
    @NotNull public final CompilerOutputProvider myAnnotator;
    @NotNull public final PsiFile mySourcePsiFile; // original file
    @NotNull public final Editor myEditor;
    @NotNull public final String myHomePath;
    @NotNull public final String myTargetFile; // path to the file, relative
    @Nullable public final PsiFile myTargetMli;
    @NotNull public final String myOutputFolder;

    public CollectedInfo(@NotNull CompilerOutputProvider annotator, @NotNull PsiFile sourcePsiFile,
                         @NotNull Editor editor, @NotNull String homePath,
                         @NotNull String targetFile, @Nullable PsiFile mli, @NotNull String outputFolder) {
        myAnnotator = annotator;
        mySourcePsiFile = sourcePsiFile;
        myEditor = editor;
        myHomePath = homePath;
        myTargetFile = targetFile;
        myTargetMli = mli;
        myOutputFolder = outputFolder;
    }
}
