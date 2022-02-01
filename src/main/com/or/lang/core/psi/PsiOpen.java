package com.or.lang.core.psi;

import com.intellij.psi.StubBasedPsiElement;
import com.or.lang.core.stub.PsiOpenStub;
import org.jetbrains.annotations.NotNull;

public interface PsiOpen extends PsiStructuredElement, StubBasedPsiElement<PsiOpenStub> {
    @NotNull String getPath();

}
