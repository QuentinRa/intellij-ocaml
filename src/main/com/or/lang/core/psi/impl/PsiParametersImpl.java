package com.or.lang.core.psi.impl;

import com.intellij.psi.tree.IElementType;
import com.or.lang.core.CompositeTypePsiElement;
import com.or.lang.core.ORUtil;
import com.or.lang.core.psi.PsiParameter;
import com.or.lang.core.psi.PsiParameters;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PsiParametersImpl extends CompositeTypePsiElement implements PsiParameters {

    protected PsiParametersImpl(@NotNull IElementType elementType) {
        super(elementType);
    }

    @Override
    public @NotNull List<PsiParameter> getParametersList() {
        return ORUtil.findImmediateChildrenOfClass(this, PsiParameter.class);
    }

    @NotNull
    @Override
    public String toString() {
        return "Parameters";
    }
}
