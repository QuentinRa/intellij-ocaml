// This is a generated file. Not intended for manual editing.
package com.ocaml.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface OCamlLetModuleBaseExpr extends OCamlExpr {

  @Nullable
  OCamlExpr getExpr();

  @NotNull
  List<OCamlModuleName> getModuleNameList();

  @NotNull
  OCamlModuleExpr getModuleExpr();

  @NotNull
  List<OCamlModuleType> getModuleTypeList();

}
