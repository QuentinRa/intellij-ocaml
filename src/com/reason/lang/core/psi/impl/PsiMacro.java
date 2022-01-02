package com.reason.lang.core.psi.impl;

import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.*;
import com.intellij.psi.tree.*;
import com.reason.lang.core.*;
import org.jetbrains.annotations.*;

public class PsiMacro extends CompositePsiElement {
    protected PsiMacro(IElementType type) {
        super(type);
    }

    @Override
    public @Nullable String getName() {
        PsiElement name = ORUtil.findImmediateFirstChildOfClass(this, PsiMacroName.class);
        return name == null ? null : name.getText();
    }

    public @Nullable PsiMacroBody getContent() {
        return ORUtil.findImmediateFirstChildOfClass(this, PsiMacroBody.class);
    }

    public boolean isRoot() {
        PsiElement name = ORUtil.findImmediateFirstChildOfClass(this, PsiMacroName.class);
        if (name == null) {
            return false;
        }

        return name.getText().startsWith("%%");
    }

    @Override
    public @NotNull String toString() {
        return "PsiMacro";
    }
}
