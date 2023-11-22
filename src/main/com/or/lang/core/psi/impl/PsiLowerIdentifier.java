package com.or.lang.core.psi.impl;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.IncorrectOperationException;
import com.or.lang.core.CompositeTypePsiElement;
import com.or.lang.core.ORUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PsiLowerIdentifier extends CompositeTypePsiElement implements PsiNameIdentifierOwner {

    // region Constructors
    public PsiLowerIdentifier(@NotNull IElementType elementType) {
        super(elementType);
    }
    // endregion

    // region NamedElement
    @Override
    public String getName() {
        return getText();
    }

    @Override
    public @Nullable PsiElement getNameIdentifier() {
        return this;
    }

    @Override
    public @NotNull PsiElement setName(@NotNull String newName) throws IncorrectOperationException {
        ORUtil.renameNodeWithLIDENT(this, newName);
        return this;
    }
    // endregion

    @Override
    public @NotNull String toString() {
        return "LID";
    }
}
