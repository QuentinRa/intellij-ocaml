package com.ocaml.lang.core;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

public abstract class OCamlElementImpl extends ASTWrapperPsiElement implements OCamlElement {

    public OCamlElementImpl(@NotNull ASTNode node) {
        super(node);
    }
}