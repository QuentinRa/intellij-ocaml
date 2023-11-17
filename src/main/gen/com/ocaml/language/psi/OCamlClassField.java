// This is a generated file. Not intended for manual editing.
package com.ocaml.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface OCamlClassField extends PsiElement {

  @Nullable
  OCamlClassExpr getClassExpr();

  @Nullable
  OCamlExpr getExpr();

  @Nullable
  OCamlInstVarName getInstVarName();

  @Nullable
  OCamlMethodName getMethodName();

  @NotNull
  List<OCamlParameter> getParameterList();

  @Nullable
  OCamlPolyTypexpr getPolyTypexpr();

  @Nullable
  OCamlTypexpr getTypexpr();

}
