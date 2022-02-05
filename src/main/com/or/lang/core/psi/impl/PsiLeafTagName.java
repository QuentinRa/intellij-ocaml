package com.or.lang.core.psi.impl;

import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

public class PsiLeafTagName extends LeafPsiElement {
    public PsiLeafTagName(@NotNull IElementType type, CharSequence text) {
        super(type, text);
    }

    @Override
    public @NotNull String toString() {
        return "TagName:" + getText();
    }
}
