package com.or.lang.core.psi.impl;

import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.or.lang.core.CompositeTypePsiElement;
import com.or.lang.core.psi.PsiTagStart;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class PsiTagStartImpl extends CompositeTypePsiElement implements PsiTagStart {
    protected PsiTagStartImpl(@NotNull IElementType elementType) {
        super(elementType);
    }

    @Override
    public @Nullable PsiElement getNameIdentifier() {
        PsiElement lastTag = null;

        Collection<PsiLeafTagName> tags = PsiTreeUtil.findChildrenOfType(this, PsiLeafTagName.class);
        if (!tags.isEmpty()) {
            for (PsiLeafTagName tag : tags) {
                PsiElement currentStart = tag.getParent().getParent();
                if (currentStart == this) {
                    lastTag = tag.getParent();
                }
            }
        }

        return lastTag;
    }

    @Override
    public @Nullable String getName() {
        PsiElement nameIdentifier = getNameIdentifier();
        return nameIdentifier == null ? null : nameIdentifier.getText();
    }

    @Override
    public @Nullable PsiElement setName(@NotNull String name) throws IncorrectOperationException {
        return null;
    }

    @Override
    public @NotNull String toString() {
        return "Tag start";
    }
}
