// This is a generated file. Not intended for manual editing.
package com.ocaml.language.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.ocaml.language.psi.OCamlTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.ocaml.language.psi.*;

public class OCamlExceptionDefinitionImpl extends ASTWrapperPsiElement implements OCamlExceptionDefinition {

  public OCamlExceptionDefinitionImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull OCamlVisitor visitor) {
    visitor.visitExceptionDefinition(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof OCamlVisitor) accept((OCamlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public OCamlConstr getConstr() {
    return findChildByClass(OCamlConstr.class);
  }

  @Override
  @Nullable
  public OCamlConstrDecl getConstrDecl() {
    return findChildByClass(OCamlConstrDecl.class);
  }

  @Override
  @Nullable
  public OCamlConstrName getConstrName() {
    return findChildByClass(OCamlConstrName.class);
  }

}
