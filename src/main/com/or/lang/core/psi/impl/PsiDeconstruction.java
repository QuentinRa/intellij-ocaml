package com.or.lang.core.psi.impl;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.tree.IElementType;
import com.or.lang.OCamlTypes;
import com.or.lang.core.CompositeTypePsiElement;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PsiDeconstruction extends CompositeTypePsiElement {

    protected PsiDeconstruction(@NotNull IElementType elementType) {
        super(elementType);
    }

    public @NotNull List<PsiElement> getDeconstructedElements() {
        List<PsiElement> result = new ArrayList<>();
        for (PsiElement child : getChildren()) {
            IElementType elementType = child.getNode().getElementType();
            if (elementType != OCamlTypes.LPAREN
                    && elementType != OCamlTypes.COMMA
                    && elementType != OCamlTypes.RPAREN
                    && elementType != OCamlTypes.UNDERSCORE
                    && !(child instanceof PsiWhiteSpace)) {
                result.add(child);
            }
        }
        return result;
    }
}
