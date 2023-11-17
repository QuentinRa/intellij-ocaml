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

public class OCamlLetBindingImpl extends ASTWrapperPsiElement implements OCamlLetBinding {

  public OCamlLetBindingImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull OCamlVisitor visitor) {
    visitor.visitLetBinding(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof OCamlVisitor) accept((OCamlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public OCamlExpr getExpr() {
    return findNotNullChildByClass(OCamlExpr.class);
  }

  @Override
  @NotNull
  public List<OCamlParameter> getParameterList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, OCamlParameter.class);
  }

  @Override
  @Nullable
  public OCamlPattern getPattern() {
    return findChildByClass(OCamlPattern.class);
  }

  @Override
  @Nullable
  public OCamlPolyTypexpr getPolyTypexpr() {
    return findChildByClass(OCamlPolyTypexpr.class);
  }

  @Override
  @NotNull
  public List<OCamlTypexpr> getTypexprList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, OCamlTypexpr.class);
  }

  @Override
  @Nullable
  public OCamlValueName getValueName() {
    return findChildByClass(OCamlValueName.class);
  }

}
