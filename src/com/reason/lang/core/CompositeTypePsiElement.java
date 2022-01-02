package com.reason.lang.core;

import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.*;
import com.intellij.psi.tree.*;
import org.jetbrains.annotations.*;

public abstract class CompositeTypePsiElement<T> extends CompositePsiElement {
    protected final @NotNull T m_types;

    protected CompositeTypePsiElement(@NotNull T types, @NotNull IElementType elementType) {
        super(elementType);
        m_types = types;
    }

    protected @Nullable <C extends PsiElement> C findChildByClass(@NotNull Class<C> clazz) {
        return ORUtil.findImmediateFirstChildOfClass(this, clazz);
    }
}
