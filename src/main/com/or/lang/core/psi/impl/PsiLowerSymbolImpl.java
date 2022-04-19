package com.or.lang.core.psi.impl;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.PsiReference;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.IncorrectOperationException;
import com.or.lang.core.CompositeTypePsiElement;
import com.or.lang.core.ORUtil;
import com.or.lang.core.psi.PsiLowerSymbol;
import com.or.lang.core.psi.reference.PsiLowerSymbolReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PsiLowerSymbolImpl extends CompositeTypePsiElement implements PsiLowerSymbol, PsiNameIdentifierOwner {

    // region Constructors
    protected PsiLowerSymbolImpl(@NotNull IElementType elementType) {
        super(elementType);
    }
    // endregion

    @Override
    public PsiReference getReference() {
        return new PsiLowerSymbolReference(this);
    }

    @Nullable
    @Override
    public String toString() {
        return "LSymbol";
    }

    // PsiNameIdentifierOwner
    @Override public @NotNull PsiElement getNameIdentifier() {
        return this;
    }

    @Override public PsiElement setName(@NotNull String newName) throws IncorrectOperationException {
        ORUtil.renameNodeWithLIDENT(this, newName);
        return this;
    }

    @Override public String getName() {
        return getNameIdentifier().getText();
    }
}
