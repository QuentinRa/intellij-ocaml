package com.or.lang.core.psi.impl;

import com.intellij.psi.impl.source.tree.CompositePsiElement;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

public class PsiLocalOpen extends CompositePsiElement {

    protected PsiLocalOpen(IElementType type) {
        super(type);
    }

    @NotNull
    @Override
    public String toString() {
        return "Local open";
    }
}
