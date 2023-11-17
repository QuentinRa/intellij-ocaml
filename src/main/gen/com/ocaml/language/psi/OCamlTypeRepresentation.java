// This is a generated file. Not intended for manual editing.
package com.ocaml.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface OCamlTypeRepresentation extends PsiElement {

  @NotNull
  List<OCamlConstrDecl> getConstrDeclList();

  @Nullable
  OCamlRecordDecl getRecordDecl();

}
