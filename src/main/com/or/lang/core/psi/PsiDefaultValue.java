package com.or.lang.core.psi;

import com.intellij.psi.impl.source.tree.CompositePsiElement;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

public class PsiDefaultValue extends CompositePsiElement {
    public PsiDefaultValue(@NotNull IElementType elementType) {
        super(elementType);
    }

    @Override
    public @NotNull String toString() {
        return "Default value";
    }
}