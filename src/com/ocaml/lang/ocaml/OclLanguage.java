package com.ocaml.lang.ocaml;

import com.intellij.lang.Language;

public class OclLanguage extends Language {
  public static final OclLanguage INSTANCE = new OclLanguage();

  private OclLanguage() {
    super("OCaml");
  }
}
