// This is a generated file. Not intended for manual editing.
package com.ocaml.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface OCamlPolymorphicVariantType extends PsiElement {

  @NotNull
  List<OCamlTagName> getTagNameList();

  @NotNull
  List<OCamlTagSpec> getTagSpecList();

  @Nullable
  OCamlTagSpecFirst getTagSpecFirst();

  @NotNull
  List<OCamlTagSpecFull> getTagSpecFullList();

}
