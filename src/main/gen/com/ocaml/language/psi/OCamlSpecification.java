// This is a generated file. Not intended for manual editing.
package com.ocaml.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface OCamlSpecification extends PsiElement {

  @Nullable
  OCamlClassSpecification getClassSpecification();

  @Nullable
  OCamlClasstypeDefinition getClasstypeDefinition();

  @Nullable
  OCamlConstrDecl getConstrDecl();

  @Nullable
  OCamlExtendedModulePath getExtendedModulePath();

  @Nullable
  OCamlExternalDeclaration getExternalDeclaration();

  @Nullable
  OCamlModtypeName getModtypeName();

  @Nullable
  OCamlModuleName getModuleName();

  @Nullable
  OCamlModulePath getModulePath();

  @Nullable
  OCamlModuleType getModuleType();

  @Nullable
  OCamlRecursiveModuleExtensionSpec getRecursiveModuleExtensionSpec();

  @Nullable
  OCamlTypeDefinition getTypeDefinition();

  @Nullable
  OCamlTypexpr getTypexpr();

  @Nullable
  OCamlValueName getValueName();

}
