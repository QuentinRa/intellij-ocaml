package com.ocaml.lang.core;

import com.intellij.extapi.psi.*;
import com.intellij.lang.*;
import org.jetbrains.annotations.*;

public abstract class OclElementImpl extends ASTWrapperPsiElement implements OclElement {
    public OclElementImpl(@NotNull ASTNode node) {
        super(node);
    }
}