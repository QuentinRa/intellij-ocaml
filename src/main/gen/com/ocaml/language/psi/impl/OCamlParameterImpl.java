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

public class OCamlParameterImpl extends ASTWrapperPsiElement implements OCamlParameter {

  public OCamlParameterImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull OCamlVisitor visitor) {
    visitor.visitParameter(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof OCamlVisitor) accept((OCamlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public OCamlExpr getExpr() {
    return findChildByClass(OCamlExpr.class);
  }

  @Override
  @Nullable
  public OCamlLabelName getLabelName() {
    return findChildByClass(OCamlLabelName.class);
  }

  @Override
  @Nullable
  public OCamlPattern getPattern() {
    return findChildByClass(OCamlPattern.class);
  }

  @Override
  @Nullable
  public OCamlTypexpr getTypexpr() {
    return findChildByClass(OCamlTypexpr.class);
  }

}
