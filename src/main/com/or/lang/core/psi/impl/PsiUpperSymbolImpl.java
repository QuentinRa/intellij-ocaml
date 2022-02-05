package com.or.lang.core.psi.impl;

import com.intellij.psi.PsiReference;
import com.intellij.psi.tree.IElementType;
import com.or.ide.files.FileBase;
import com.or.lang.core.CompositeTypePsiElement;
import com.or.lang.core.psi.PsiUpperSymbol;
import com.or.lang.core.psi.reference.PsiUpperSymbolReference;
import org.jetbrains.annotations.NotNull;

public class PsiUpperSymbolImpl extends CompositeTypePsiElement implements PsiUpperSymbol {

    // region Constructors
    protected PsiUpperSymbolImpl(@NotNull IElementType elementType) {
        super(elementType);
    }
    // endregion

    @Override
    public PsiReference getReference() {
        return new PsiUpperSymbolReference(this);
    }

    @NotNull
    @Override
    public String toString() {
        String name = getText();
        return "USymbol "
                + (name.isEmpty() ? "<" + ((FileBase) getContainingFile()).getModuleName() + ">" : name);
    }
}
