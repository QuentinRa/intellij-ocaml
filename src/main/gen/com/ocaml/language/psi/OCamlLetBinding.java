// This is a generated file. Not intended for manual editing.
package com.ocaml.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface OCamlLetBinding extends PsiElement {

  @NotNull
  OCamlExpr getExpr();

  @NotNull
  List<OCamlParameter> getParameterList();

  @Nullable
  OCamlPattern getPattern();

  @Nullable
  OCamlPolyTypexpr getPolyTypexpr();

  @NotNull
  List<OCamlTypexpr> getTypexprList();

  @Nullable
  OCamlValueName getValueName();

}
