package com.or.lang.core.psi;

import org.jetbrains.annotations.Nullable;

public interface PsiSignatureElement {
    @Nullable
    PsiSignature getSignature();
}
