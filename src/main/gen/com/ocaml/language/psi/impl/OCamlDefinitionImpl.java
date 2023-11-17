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

public class OCamlDefinitionImpl extends ASTWrapperPsiElement implements OCamlDefinition {

  public OCamlDefinitionImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull OCamlVisitor visitor) {
    visitor.visitDefinition(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof OCamlVisitor) accept((OCamlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public OCamlClassDefinition getClassDefinition() {
    return findChildByClass(OCamlClassDefinition.class);
  }

  @Override
  @Nullable
  public OCamlClasstypeDefinition getClasstypeDefinition() {
    return findChildByClass(OCamlClasstypeDefinition.class);
  }

  @Override
  @Nullable
  public OCamlExceptionDefinition getExceptionDefinition() {
    return findChildByClass(OCamlExceptionDefinition.class);
  }

  @Override
  @Nullable
  public OCamlExternalDeclaration getExternalDeclaration() {
    return findChildByClass(OCamlExternalDeclaration.class);
  }

  @Override
  @NotNull
  public List<OCamlLetBinding> getLetBindingList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, OCamlLetBinding.class);
  }

  @Override
  @Nullable
  public OCamlModtypeName getModtypeName() {
    return findChildByClass(OCamlModtypeName.class);
  }

  @Override
  @NotNull
  public List<OCamlModuleName> getModuleNameList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, OCamlModuleName.class);
  }

  @Override
  @Nullable
  public OCamlModulePath getModulePath() {
    return findChildByClass(OCamlModulePath.class);
  }

  @Override
  @Nullable
  public OCamlModuleExpr getModuleExpr() {
    return findChildByClass(OCamlModuleExpr.class);
  }

  @Override
  @NotNull
  public List<OCamlModuleType> getModuleTypeList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, OCamlModuleType.class);
  }

  @Override
  @Nullable
  public OCamlRecursiveModuleExtensionDef getRecursiveModuleExtensionDef() {
    return findChildByClass(OCamlRecursiveModuleExtensionDef.class);
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
