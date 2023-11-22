package com.or.lang.core.psi.impl;

import com.intellij.psi.tree.IElementType;
import com.or.lang.core.CompositeTypePsiElement;
import org.jetbrains.annotations.NotNull;

public class PsiModuleType extends CompositeTypePsiElement {

    protected PsiModuleType(@NotNull IElementType elementType) {
        super(elementType);
    }

    @Override
    public @NotNull String toString() {
        return "PsiModuleType";
    }
}
