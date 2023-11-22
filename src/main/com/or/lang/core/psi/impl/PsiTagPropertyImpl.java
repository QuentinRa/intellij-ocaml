package com.or.lang.core.psi.impl;

import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.or.lang.OCamlTypes;
import com.or.lang.core.CompositeTypePsiElement;
import com.or.lang.core.ORUtil;
import com.or.lang.core.psi.PsiTagProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PsiTagPropertyImpl extends CompositeTypePsiElement implements PsiTagProperty {

    // region Constructors
    protected PsiTagPropertyImpl(@NotNull IElementType elementType) {
        super(elementType);
    }
    // endregion

    @Nullable
    private PsiElement getNameElement() {
        return ORUtil.findImmediateFirstChildOfType(this, OCamlTypes.PROPERTY_NAME);
    }

    @NotNull public String getName() {
        PsiElement nameElement = getNameElement();
        return nameElement == null ? "" : nameElement.getText();
    }

    @NotNull
    @Override
    public String toString() {
        return "TagProperty " + getName();
    }
}
