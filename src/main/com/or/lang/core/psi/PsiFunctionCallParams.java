package com.or.lang.core.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface PsiFunctionCallParams extends PsiElement {
    @NotNull
    List<PsiParameter> getParametersList();
}
