// This is a generated file. Not intended for manual editing.
package com.ocaml.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface OCamlClassBinding extends PsiElement {

  @NotNull
  OCamlClassName getClassName();

  @Nullable
  OCamlClassType getClassType();

  @NotNull
  OCamlClassExpr getClassExpr();

  @NotNull
  OCamlParameter getParameter();

  @Nullable
  OCamlTypeParameters getTypeParameters();

}
