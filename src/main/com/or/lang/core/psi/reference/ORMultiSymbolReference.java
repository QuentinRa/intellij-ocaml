package com.or.lang.core.psi.reference;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiPolyVariantReferenceBase;
import com.intellij.psi.ResolveResult;
import com.or.ide.files.FileBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ORMultiSymbolReference<T extends PsiElement> extends PsiPolyVariantReferenceBase<T> {
    protected final @Nullable String myReferenceName;

    protected ORMultiSymbolReference(@NotNull T element) {
        super(element, TextRange.create(0, element.getTextLength()));
        myReferenceName = element.getText();
    }

    @Override
    public @Nullable PsiElement resolve() {
        ResolveResult[] resolveResults = multiResolve(false);
        return 0 < resolveResults.length ? resolveResults[0].getElement() : null;
    }

    public @Nullable PsiElement resolveInterface() {
        ResolveResult[] resolveResults = multiResolve(false);

        if (resolveResults.length < 1) {
            return null;
        }

        if (resolveResults.length == 1) {
            return resolveResults[0].getElement();
        }

        // Look into other resolved elements to find an equivalent interface if one exist
        for (ResolveResult resolved : resolveResults) {
            PsiElement element = resolved.getElement();
            FileBase file = element == null ? null : (FileBase) element.getContainingFile();
            if (file != null && file.isInterface()) {
                return element;
            }
        }

        return resolveResults[0].getElement();
    }
}
