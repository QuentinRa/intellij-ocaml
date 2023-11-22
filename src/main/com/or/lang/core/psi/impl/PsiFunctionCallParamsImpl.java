package com.or.lang.core.psi.impl;

import com.intellij.psi.tree.IElementType;
import com.or.lang.core.CompositeTypePsiElement;
import com.or.lang.core.ORUtil;
import com.or.lang.core.psi.PsiFunctionCallParams;
import com.or.lang.core.psi.PsiParameter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PsiFunctionCallParamsImpl extends CompositeTypePsiElement implements PsiFunctionCallParams {

    protected PsiFunctionCallParamsImpl(@NotNull IElementType elementType) {
        super(elementType);
    }

    @Override
    @NotNull
    public List<PsiParameter> getParametersList() {
        return ORUtil.findImmediateChildrenOfClass(this, PsiParameter.class);
    }

    @NotNull
    @Override
    public String toString() {
        return "Function call parameters";
    }
}
