// This is a generated file. Not intended for manual editing.
package com.ocaml.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface OCamlDefinition extends PsiElement {

  @Nullable
  OCamlClassDefinition getClassDefinition();

  @Nullable
  OCamlClasstypeDefinition getClasstypeDefinition();

  @Nullable
  OCamlExceptionDefinition getExceptionDefinition();

  @Nullable
  OCamlExternalDeclaration getExternalDeclaration();

  @NotNull
  List<OCamlLetBinding> getLetBindingList();

  @Nullable
  OCamlModtypeName getModtypeName();

  @NotNull
  List<OCamlModuleName> getModuleNameList();

  @Nullable
  OCamlModulePath getModulePath();

  @Nullable
  OCamlModuleExpr getModuleExpr();

  @NotNull
  List<OCamlModuleType> getModuleTypeList();

  @Nullable
  OCamlRecursiveModuleExtensionDef getRecursiveModuleExtensionDef();

  @Nullable
  OCamlTypeDefinition getTypeDefinition();

  @Nullable
  OCamlTypexpr getTypexpr();

  @Nullable
  OCamlValueName getValueName();

}
