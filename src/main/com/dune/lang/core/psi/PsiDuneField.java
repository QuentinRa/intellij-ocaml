package com.dune.lang.core.psi;

import com.dune.lang.core.psi.impl.DuneTypes;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PsiDuneField extends ASTWrapperPsiElement implements PsiNameIdentifierOwner {
    public PsiDuneField(@NotNull ASTNode node) {
        super(node);
    }

    @Nullable @Override public PsiElement getNameIdentifier() {
        PsiElement firstChild = getFirstChild();
        PsiElement nextSibling = firstChild.getNextSibling();
        return nextSibling != null && nextSibling.getNode().getElementType() == DuneTypes.ATOM
                ? nextSibling
                : null;
    }

    @Nullable @Override public String getName() {
        PsiElement identifier = getNameIdentifier();
        return identifier == null ? null : identifier.getText();
    }

    @Override
    public @Nullable PsiElement setName(@NotNull String name) throws IncorrectOperationException {
        return null;
    }

    @Override public @Nullable String toString() {
        return "Field " + getName();
    }
}
