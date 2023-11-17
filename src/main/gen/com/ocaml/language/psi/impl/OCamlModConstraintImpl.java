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

public class OCamlModConstraintImpl extends ASTWrapperPsiElement implements OCamlModConstraint {

  public OCamlModConstraintImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull OCamlVisitor visitor) {
    visitor.visitModConstraint(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof OCamlVisitor) accept((OCamlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public OCamlExtendedModulePath getExtendedModulePath() {
    return findChildByClass(OCamlExtendedModulePath.class);
  }

  @Override
  @Nullable
  public OCamlModulePath getModulePath() {
    return findChildByClass(OCamlModulePath.class);
  }

  @Override
  @Nullable
  public OCamlTypeConstraint getTypeConstraint() {
    return findChildByClass(OCamlTypeConstraint.class);
  }

  @Override
  @Nullable
  public OCamlTypeEquation getTypeEquation() {
    return findChildByClass(OCamlTypeEquation.class);
  }

  @Override
  @Nullable
  public OCamlTypeParams getTypeParams() {
    return findChildByClass(OCamlTypeParams.class);
  }

  @Override
  @Nullable
  public OCamlTypeconstr getTypeconstr() {
    return findChildByClass(OCamlTypeconstr.class);
  }

}
