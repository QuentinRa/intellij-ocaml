package com.or.lang.core;

import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.CompositePsiElement;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class CompositeTypePsiElement extends CompositePsiElement {
    protected CompositeTypePsiElement(@NotNull IElementType elementType) {
        super(elementType);
    }

    protected @Nullable <C extends PsiElement> C findChildByClass(@NotNull Class<C> clazz) {
        return ORUtil.findImmediateFirstChildOfClass(this, clazz);
    }
}
