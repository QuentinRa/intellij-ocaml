// This is a generated file. Not intended for manual editing.
package com.ocaml.language.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.ocaml.language.psi.OCamlTypes.*;
import com.ocaml.language.psi.*;

public class OCamlObjectExprInternalBaseExprImpl extends OCamlExprImpl implements OCamlObjectExprInternalBaseExpr {

  public OCamlObjectExprInternalBaseExprImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull OCamlVisitor visitor) {
    visitor.visitObjectExprInternalBaseExpr(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof OCamlVisitor) accept((OCamlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public OCamlClassBody getClassBody() {
    return findChildByClass(OCamlClassBody.class);
  }

  @Override
  @Nullable
  public OCamlClassPath getClassPath() {
    return findChildByClass(OCamlClassPath.class);
  }

  @Override
  @Nullable
  public OCamlExpr getExpr() {
    return findChildByClass(OCamlExpr.class);
  }

  @Override
  @Nullable
  public OCamlInstVarName getInstVarName() {
    return findChildByClass(OCamlInstVarName.class);
  }

}
