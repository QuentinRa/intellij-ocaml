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

public class OCamlConstantBaseExprImpl extends OCamlExprImpl implements OCamlConstantBaseExpr {

  public OCamlConstantBaseExprImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull OCamlVisitor visitor) {
    visitor.visitConstantBaseExpr(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof OCamlVisitor) accept((OCamlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public OCamlCharLiteral getCharLiteral() {
    return findChildByClass(OCamlCharLiteral.class);
  }

  @Override
  @Nullable
  public OCamlConstr getConstr() {
    return findChildByClass(OCamlConstr.class);
  }

  @Override
  @Nullable
  public OCamlFloatLiteral getFloatLiteral() {
    return findChildByClass(OCamlFloatLiteral.class);
  }

  @Override
  @Nullable
  public OCamlInt32Literal getInt32Literal() {
    return findChildByClass(OCamlInt32Literal.class);
  }

  @Override
  @Nullable
  public OCamlInt64Literal getInt64Literal() {
    return findChildByClass(OCamlInt64Literal.class);
  }

  @Override
  @Nullable
  public OCamlIntegerLiteral getIntegerLiteral() {
    return findChildByClass(OCamlIntegerLiteral.class);
  }

  @Override
  @Nullable
  public OCamlNativeintLiteral getNativeintLiteral() {
    return findChildByClass(OCamlNativeintLiteral.class);
  }

  @Override
  @Nullable
  public OCamlStringLiteral getStringLiteral() {
    return findChildByClass(OCamlStringLiteral.class);
  }

  @Override
  @Nullable
  public OCamlTagName getTagName() {
    return findChildByClass(OCamlTagName.class);
  }

}
