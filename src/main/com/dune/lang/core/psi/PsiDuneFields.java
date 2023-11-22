package com.dune.lang.core.psi;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

public class PsiDuneFields extends ASTWrapperPsiElement {
    public PsiDuneFields(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public @NotNull String toString() {
        return "Fields";
    }
}
