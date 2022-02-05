package com.or.lang.core.psi.impl;

import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.CompositePsiElement;
import com.intellij.psi.tree.IElementType;
import com.or.lang.core.ORUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PsiMacro extends CompositePsiElement {
    protected PsiMacro(IElementType type) {
        super(type);
    }

    @Override
    public @Nullable String getName() {
        PsiElement name = ORUtil.findImmediateFirstChildOfClass(this, PsiMacroName.class);
        return name == null ? null : name.getText();
    }

    @Override
    public @NotNull String toString() {
        return "PsiMacro";
    }
}
