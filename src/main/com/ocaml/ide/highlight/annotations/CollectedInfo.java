package com.ocaml.ide.highlight.annotations;

import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

class CollectedInfo {
    public final PsiFile mySourcePsiFile; // original file
    public final Editor myEditor;
    public final String myHomePath;

    public CollectedInfo(PsiFile sourcePsiFile, @NotNull Editor editor, @NotNull String homePath) {
        mySourcePsiFile = sourcePsiFile;
        myEditor = editor;
        myHomePath = homePath;
    }
}
