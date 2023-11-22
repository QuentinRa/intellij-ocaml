package com.or.lang.core.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import com.or.lang.core.stub.PsiIncludeStub;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PsiInclude extends PsiStructuredElement, StubBasedPsiElement<PsiIncludeStub> {
    String[] getQualifiedPath();

    String @Nullable [] getResolvedPath();

    @NotNull String getIncludePath();

    @Nullable PsiUpperSymbol getModuleReference();

    @Nullable PsiElement resolveModule();

}
