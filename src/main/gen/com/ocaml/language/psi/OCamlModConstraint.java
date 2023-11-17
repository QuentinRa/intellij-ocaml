// This is a generated file. Not intended for manual editing.
package com.ocaml.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface OCamlModConstraint extends PsiElement {

  @Nullable
  OCamlExtendedModulePath getExtendedModulePath();

  @Nullable
  OCamlModulePath getModulePath();

  @Nullable
  OCamlTypeConstraint getTypeConstraint();

  @Nullable
  OCamlTypeEquation getTypeEquation();

  @Nullable
  OCamlTypeParams getTypeParams();

  @Nullable
  OCamlTypeconstr getTypeconstr();

}
