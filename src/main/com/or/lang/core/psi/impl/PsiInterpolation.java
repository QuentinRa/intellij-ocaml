package com.or.lang.core.psi.impl;

import com.intellij.psi.impl.source.tree.CompositePsiElement;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

public class PsiInterpolation extends CompositePsiElement {
    protected PsiInterpolation(IElementType type) {
        super(type);
    }

    @Override
    public @NotNull String toString() {
        return "Interpolation";
    }
}
