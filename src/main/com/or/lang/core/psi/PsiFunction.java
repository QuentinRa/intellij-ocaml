package com.or.lang.core.psi;

import com.intellij.psi.PsiElement;
import com.or.lang.core.psi.impl.PsiFunctionBody;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface PsiFunction extends PsiElement {
    @NotNull
    List<PsiParameter> getParameters();

    @Nullable
    PsiFunctionBody getBody();
}
