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

public class OCamlPrefixBaseExprImpl extends OCamlExprImpl implements OCamlPrefixBaseExpr {

  public OCamlPrefixBaseExprImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull OCamlVisitor visitor) {
    visitor.visitPrefixBaseExpr(this);
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
  public OCamlPrefixSymbol getPrefixSymbol() {
    return findNotNullChildByClass(OCamlPrefixSymbol.class);
  }

}
