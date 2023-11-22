package com.or.lang.core.psi.impl;

import com.intellij.psi.tree.IElementType;
import com.or.lang.core.CompositeTypePsiElement;
import com.or.lang.core.ORUtil;
import com.or.lang.core.psi.PsiTagStart;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PsiTag extends CompositeTypePsiElement {
    protected PsiTag(@NotNull IElementType elementType) {
        super(elementType);
    }

    @Override
    public @Nullable String getName() {
        PsiTagStart tagStart = ORUtil.findImmediateFirstChildOfClass(this, PsiTagStart.class);
        return tagStart == null ? null : ORUtil.getTextUntilWhitespace(tagStart.getFirstChild().getNextSibling());
    }

    @NotNull
    @Override
    public String toString() {
        return "Tag";
    }
}
