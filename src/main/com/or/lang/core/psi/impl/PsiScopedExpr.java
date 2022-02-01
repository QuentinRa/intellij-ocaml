package com.or.lang.core.psi.impl;

import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.or.lang.OCamlTypes;
import com.or.lang.core.CompositeTypePsiElement;
import org.jetbrains.annotations.NotNull;

public class PsiScopedExpr extends CompositeTypePsiElement {

    protected PsiScopedExpr(@NotNull IElementType elementType) {
        super(elementType);
    }

    public boolean isEmpty() {
        PsiElement firstChild = getFirstChild();
        IElementType firstType = firstChild == null ? null : firstChild.getNode().getElementType();
        if (firstType == OCamlTypes.LPAREN) {
            PsiElement secondChild = firstChild.getNextSibling();
            IElementType secondType = secondChild == null ? null : secondChild.getNode().getElementType();
            return secondType == OCamlTypes.RPAREN;
        }

        return false;
    }

    @NotNull
    @Override
    public String toString() {
        return "Scoped expression";
    }
}
