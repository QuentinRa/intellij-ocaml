package com.or.lang.core.psi.impl;

import com.intellij.psi.PsiReference;
import com.intellij.psi.tree.IElementType;
import com.or.lang.core.CompositeTypePsiElement;
import com.or.lang.core.psi.PsiLowerSymbol;
import com.or.lang.core.psi.reference.PsiLowerSymbolReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PsiLowerSymbolImpl extends CompositeTypePsiElement implements PsiLowerSymbol {

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
}
