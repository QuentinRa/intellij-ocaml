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

public class OCamlSpecificationImpl extends ASTWrapperPsiElement implements OCamlSpecification {

  public OCamlSpecificationImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull OCamlVisitor visitor) {
    visitor.visitSpecification(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof OCamlVisitor) accept((OCamlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public OCamlClassSpecification getClassSpecification() {
    return findChildByClass(OCamlClassSpecification.class);
  }

  @Override
  @Nullable
  public OCamlClasstypeDefinition getClasstypeDefinition() {
    return findChildByClass(OCamlClasstypeDefinition.class);
  }

  @Override
  @Nullable
  public OCamlConstrDecl getConstrDecl() {
    return findChildByClass(OCamlConstrDecl.class);
  }

  @Override
  @Nullable
  public OCamlExtendedModulePath getExtendedModulePath() {
    return findChildByClass(OCamlExtendedModulePath.class);
  }

  @Override
  @Nullable
  public OCamlExternalDeclaration getExternalDeclaration() {
    return findChildByClass(OCamlExternalDeclaration.class);
  }

  @Override
  @Nullable
  public OCamlModtypeName getModtypeName() {
    return findChildByClass(OCamlModtypeName.class);
  }

  @Override
  @Nullable
  public OCamlModuleName getModuleName() {
    return findChildByClass(OCamlModuleName.class);
  }

  @Override
  @Nullable
  public OCamlModulePath getModulePath() {
    return findChildByClass(OCamlModulePath.class);
  }

  @Override
  @Nullable
  public OCamlModuleType getModuleType() {
    return findChildByClass(OCamlModuleType.class);
  }

  @Override
  @Nullable
  public OCamlRecursiveModuleExtensionSpec getRecursiveModuleExtensionSpec() {
    return findChildByClass(OCamlRecursiveModuleExtensionSpec.class);
  }

  @Override
  @Nullable
  public OCamlTypeDefinition getTypeDefinition() {
    return findChildByClass(OCamlTypeDefinition.class);
  }

  @Override
  @Nullable
  public OCamlTypexpr getTypexpr() {
    return findChildByClass(OCamlTypexpr.class);
  }

  @Override
  @Nullable
  public OCamlValueName getValueName() {
    return findChildByClass(OCamlValueName.class);
  }

}
