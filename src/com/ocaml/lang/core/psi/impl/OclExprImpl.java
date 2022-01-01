// This is a generated file. Not intended for manual editing.
package com.ocaml.lang.core.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.ocaml.lang.core.psi.OclTypes.*;
import com.ocaml.lang.core.OclElementImpl;
import com.ocaml.lang.core.psi.*;

public class OclExprImpl extends OclElementImpl implements OclExpr {

  public OclExprImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull OclVisitor visitor) {
    visitor.visitExpr(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof OclVisitor) accept((OclVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public PsiElement getIntValue() {
    return findNotNullChildByType(INT_VALUE);
  }

  @Override
  @NotNull
  public PsiElement getLident() {
    return findNotNullChildByType(LIDENT);
  }

}
