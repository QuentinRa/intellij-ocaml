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

public class OCamlTypedefImpl extends ASTWrapperPsiElement implements OCamlTypedef {

  public OCamlTypedefImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull OCamlVisitor visitor) {
    visitor.visitTypedef(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof OCamlVisitor) accept((OCamlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public OCamlTypeInformation getTypeInformation() {
    return findNotNullChildByClass(OCamlTypeInformation.class);
  }

  @Override
  @Nullable
  public OCamlTypeParams getTypeParams() {
    return findChildByClass(OCamlTypeParams.class);
  }

  @Override
  @NotNull
  public OCamlTypeName getTypeName() {
    return findNotNullChildByClass(OCamlTypeName.class);
  }

}
