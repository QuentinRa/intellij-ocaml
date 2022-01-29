package com.dune.lang.core.psi;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

public class PsiDuneVar extends ASTWrapperPsiElement {
    public PsiDuneVar(@NotNull ASTNode node) {
        super(node);
    }
}
