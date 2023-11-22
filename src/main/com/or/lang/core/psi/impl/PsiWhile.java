package com.or.lang.core.psi.impl;

import com.intellij.psi.tree.IElementType;
import com.or.lang.core.CompositeTypePsiElement;
import org.jetbrains.annotations.NotNull;

public class PsiWhile extends CompositeTypePsiElement {

    protected PsiWhile(@NotNull IElementType elementType) {
        super(elementType);
    }

    @NotNull
    @Override
    public String toString() {
        return "While";
    }
}
