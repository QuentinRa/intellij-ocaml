// This is a generated file. Not intended for manual editing.
package com.ocaml.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface OCamlRecursiveModuleExtensionDef extends PsiElement {

  @NotNull
  List<OCamlModuleName> getModuleNameList();

  @NotNull
  List<OCamlModuleExpr> getModuleExprList();

  @NotNull
  List<OCamlModuleType> getModuleTypeList();

}
