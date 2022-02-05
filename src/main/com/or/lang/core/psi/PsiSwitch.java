package com.or.lang.core.psi;

import com.intellij.psi.PsiElement;
import com.or.lang.core.psi.impl.PsiBinaryCondition;
import org.jetbrains.annotations.Nullable;

public interface PsiSwitch extends PsiElement {
    @Nullable
    PsiBinaryCondition getCondition();

}
