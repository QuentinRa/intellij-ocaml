// This is a generated file. Not intended for manual editing.
package com.ocaml.language.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import com.ocaml.language.psi.api.OCamlElementType;
import com.ocaml.language.psi.api.OCamlTokenType;
import com.ocaml.language.psi.impl.*;

public interface OCamlTypes {

  IElementType EMPTY = new OCamlElementType("EMPTY");


  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == EMPTY) {
        return new OCamlEmptyImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
