package com.or.lang.core.psi.impl;

import com.intellij.psi.tree.IElementType;
import com.or.lang.core.CompositeTypePsiElement;
import com.or.lang.core.psi.PsiConditional;
import org.jetbrains.annotations.NotNull;

public class PsiIfStatement extends CompositeTypePsiElement implements PsiConditional {

    protected PsiIfStatement(@NotNull IElementType elementType) {
        super(elementType);
    }

}
