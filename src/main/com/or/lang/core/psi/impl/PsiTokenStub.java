package com.or.lang.core.psi.impl;

import com.intellij.extapi.psi.StubBasedPsiElementBase;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import org.jetbrains.annotations.NotNull;

public class PsiTokenStub<P extends PsiElement, S extends StubElement<P>> extends StubBasedPsiElementBase<S> {

    public PsiTokenStub(@NotNull ASTNode node) {
        super(node);
    }

    public PsiTokenStub(@NotNull S stub, @NotNull IStubElementType nodeType) {
        super(stub, nodeType);
    }
}
