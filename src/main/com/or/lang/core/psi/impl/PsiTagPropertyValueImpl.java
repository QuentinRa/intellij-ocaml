package com.or.lang.core.psi.impl;

import com.intellij.psi.tree.IElementType;
import com.or.lang.core.CompositeTypePsiElement;
import com.or.lang.core.psi.PsiTagPropertyValue;
import org.jetbrains.annotations.NotNull;

public class PsiTagPropertyValueImpl extends CompositeTypePsiElement
        implements PsiTagPropertyValue {

    // region Constructors
    protected PsiTagPropertyValueImpl(@NotNull IElementType elementType) {
        super(elementType);
    }
    // endregion

    @NotNull
    @Override
    public String toString() {
        return "TagPropertyValue";
    }
}
