// This is a generated file. Not intended for manual editing.
package com.ocaml.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface OCamlInfixOpBaseExpr extends OCamlExpr {

  @NotNull
  List<OCamlExpr> getExprList();

  @NotNull
  OCamlInfixOp getInfixOp();

}
