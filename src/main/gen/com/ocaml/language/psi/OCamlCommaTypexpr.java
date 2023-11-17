// This is a generated file. Not intended for manual editing.
package com.ocaml.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface OCamlCommaTypexpr extends OCamlTypexpr {

  @NotNull
  OCamlTypeconstr getTypeconstr();

  @NotNull
  List<OCamlTypexpr> getTypexprList();

}
