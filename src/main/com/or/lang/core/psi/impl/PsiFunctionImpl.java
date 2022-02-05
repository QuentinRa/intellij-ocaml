package com.or.lang.core.psi.impl;

import com.intellij.psi.tree.IElementType;
import com.or.lang.core.CompositeTypePsiElement;
import com.or.lang.core.ORUtil;
import com.or.lang.core.psi.PsiFunction;
import com.or.lang.core.psi.PsiParameter;
import com.or.lang.core.psi.PsiParameters;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PsiFunctionImpl extends CompositeTypePsiElement implements PsiFunction {

    protected PsiFunctionImpl(@NotNull IElementType elementType) {
        super(elementType);
    }

    @Override
    @NotNull
    public List<PsiParameter> getParameters() {
        return ORUtil.findImmediateChildrenOfClass(
                ORUtil.findImmediateFirstChildOfClass(this, PsiParameters.class), PsiParameter.class);
    }

    @Override
    @Nullable
    public PsiFunctionBody getBody() {
        return ORUtil.findImmediateFirstChildOfClass(this, PsiFunctionBody.class);
    }

    @NotNull
    @Override
    public String toString() {
        return "Function";
    }
}
