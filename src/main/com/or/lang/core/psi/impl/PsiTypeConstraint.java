package com.or.lang.core.psi.impl;

import com.intellij.psi.tree.IElementType;
import com.or.lang.core.CompositeTypePsiElement;
import org.jetbrains.annotations.NotNull;

public class PsiTypeConstraint extends CompositeTypePsiElement {
    protected PsiTypeConstraint(@NotNull IElementType elementType) {
        super(elementType);
    }
}
