package com.or.lang.core.psi.reference;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.FakePsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ORFakeResolvedElement extends FakePsiElement {
    private final @NotNull PsiElement mySourceElement;

    public ORFakeResolvedElement(@NotNull PsiElement element) {
        mySourceElement = element;
    }

    @Override
    public @NotNull PsiElement getOriginalElement() {
        return mySourceElement;
    }

    @Override
    public @Nullable PsiElement getParent() {
        return mySourceElement.getParent();
    }

    @Override
    public @Nullable String getText() {
        return mySourceElement.getText();
    }

    @Override
    public @NotNull TextRange getTextRangeInParent() {
        return TextRange.EMPTY_RANGE;
    }
}
