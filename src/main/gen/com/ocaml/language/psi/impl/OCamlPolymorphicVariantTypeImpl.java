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

public class OCamlPolymorphicVariantTypeImpl extends ASTWrapperPsiElement implements OCamlPolymorphicVariantType {

  public OCamlPolymorphicVariantTypeImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull OCamlVisitor visitor) {
    visitor.visitPolymorphicVariantType(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof OCamlVisitor) accept((OCamlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<OCamlTagName> getTagNameList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, OCamlTagName.class);
  }

  @Override
  @NotNull
  public List<OCamlTagSpec> getTagSpecList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, OCamlTagSpec.class);
  }

  @Override
  @Nullable
  public OCamlTagSpecFirst getTagSpecFirst() {
    return findChildByClass(OCamlTagSpecFirst.class);
  }

  @Override
  @NotNull
  public List<OCamlTagSpecFull> getTagSpecFullList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, OCamlTagSpecFull.class);
  }

}
