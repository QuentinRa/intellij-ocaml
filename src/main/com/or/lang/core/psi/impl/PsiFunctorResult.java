package com.or.lang.core.psi.impl;

import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.or.lang.core.CompositeTypePsiElement;
import com.or.lang.core.ORUtil;
import com.or.lang.core.psi.PsiUpperSymbol;
import com.or.lang.core.psi.reference.PsiUpperSymbolReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PsiFunctorResult extends CompositeTypePsiElement {
    protected PsiFunctorResult(@NotNull IElementType elementType) {
        super(elementType);
    }

    public @Nullable PsiUpperSymbol getModuleType() {
        return ORUtil.findImmediateLastChildOfClass(this, PsiUpperSymbol.class);
    }

    public @Nullable PsiElement resolveModule() {
        PsiUpperSymbol moduleType = getModuleType();
        PsiUpperSymbolReference reference = moduleType == null ? null : (PsiUpperSymbolReference) moduleType.getReference();
        PsiElement resolvedSymbol = reference == null ? null : reference.resolveInterface();
        if (resolvedSymbol instanceof PsiUpperIdentifier) {
            return resolvedSymbol.getParent();
        } else if (resolvedSymbol instanceof PsiFakeModule) {
            return resolvedSymbol.getContainingFile();
        }

        return null;
    }

    @Override
    public @NotNull String toString() {
        return "Functor result";
    }
}
