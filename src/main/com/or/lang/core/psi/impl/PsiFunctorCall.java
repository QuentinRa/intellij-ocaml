package com.or.lang.core.psi.impl;

import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.or.lang.core.CompositeTypePsiElement;
import com.or.lang.core.ORUtil;
import com.or.lang.core.psi.PsiFunctor;
import com.or.lang.core.psi.PsiParameters;
import com.or.lang.core.psi.PsiUpperSymbol;
import com.or.lang.core.psi.reference.PsiUpperSymbolReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PsiFunctorCall extends CompositeTypePsiElement {
    protected PsiFunctorCall(@NotNull IElementType elementType) {
        super(elementType);
    }

    @NotNull
    public String getFunctorName() {
        String text = getText();

        PsiParameters params = PsiTreeUtil.findChildOfType(this, PsiParameters.class);
        if (params == null) {
            return text;
        }

        return text.substring(0, params.getTextOffset() - getTextOffset());
    }

    public @Nullable PsiFunctor resolveFunctor() {
        PsiUpperSymbol uSymbol = ORUtil.findImmediateLastChildOfClass(this, PsiUpperSymbol.class);
        PsiUpperSymbolReference reference = uSymbol == null ? null : (PsiUpperSymbolReference) uSymbol.getReference();
        PsiElement resolvedElement = reference == null ? null : reference.resolveInterface();

        if (resolvedElement instanceof PsiUpperIdentifier) {
            PsiElement resolvedParent = resolvedElement.getParent();
            if (resolvedParent instanceof PsiFunctor) {
                return (PsiFunctor) resolvedParent;
            }
        }

        return null;
    }

    @Override
    public @NotNull String toString() {
        return "Functor call";
    }
}
