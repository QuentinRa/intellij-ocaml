package com.dune.lang.core.psi;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

public class PsiSExpr extends ASTWrapperPsiElement {

    public PsiSExpr(@NotNull ASTNode node) {
        super(node);
    }

    @NotNull @Override public String toString() {
        return "(";
    }
}
