package com.or.lang.core.psi.impl;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.or.lang.core.CompositeTypePsiElement;
import com.or.lang.core.psi.PsiSwitch;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PsiSwitchImpl extends CompositeTypePsiElement implements PsiSwitch {

    protected PsiSwitchImpl(@NotNull IElementType elementType) {
        super(elementType);
    }

    @Nullable
    public PsiBinaryCondition getCondition() {
        return PsiTreeUtil.findChildOfType(this, PsiBinaryCondition.class);
    }

    @NotNull
    @Override
    public String toString() {
        return "Switch/function";
    }
}
