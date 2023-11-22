package com.or.lang.core.psi.impl;

import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.IncorrectOperationException;
import com.or.lang.core.CompositeTypePsiElement;
import com.or.lang.core.ORUtil;
import com.or.lang.core.psi.PsiAnnotation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PsiAnnotationImpl extends CompositeTypePsiElement implements PsiAnnotation {
    protected PsiAnnotationImpl(@NotNull IElementType elementType) {
        super(elementType);
    }

    @Override
    public @Nullable PsiElement getNameIdentifier() {
        return findChildByClass(PsiMacroName.class);
    }

    @Override
    public @Nullable String getName() {
        PsiElement identifier = getNameIdentifier();
        return identifier == null ? null : identifier.getText();
    }

    @Override
    public @Nullable PsiElement getValue() {
        PsiScopedExpr expr = ORUtil.findImmediateFirstChildOfClass(this, PsiScopedExpr.class);
        if (expr != null) {
            return ORUtil.nextSibling(expr.getFirstChild());
        }
        return null;
    }

    @Override
    public @NotNull PsiElement setName(@NotNull String name) throws IncorrectOperationException {
        return this;
    }

    @Override
    public @Nullable String toString() {
        return "Annotation";
    }
}
