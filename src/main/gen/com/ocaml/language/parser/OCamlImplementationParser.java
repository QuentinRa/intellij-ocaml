// This is a generated file. Not intended for manual editing.
package com.ocaml.language.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static com.ocaml.language.psi.OCamlTypes.*;
import static com.ocaml.language.parser.OCamlParserUtils.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class OCamlImplementationParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType t, PsiBuilder b) {
    parseLight(t, b);
    return b.getTreeBuilt();
  }

  public void parseLight(IElementType t, PsiBuilder b) {
    boolean r;
    b = adapt_builder_(t, b, this, null);
    Marker m = enter_section_(b, 0, _COLLAPSE_, null);
    r = parse_root_(t, b);
    exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType t, PsiBuilder b) {
    return parse_root_(t, b, 0);
  }

  static boolean parse_root_(IElementType t, PsiBuilder b, int l) {
    return unit_implementation(b, l + 1);
  }

  /* ********************************************************** */
  // "A"
  public static boolean module_items(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "module_items")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, MODULE_ITEMS, "<module items>");
    r = consumeToken(b, "A");
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // [ module-items ]
  static boolean unit_implementation(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unit_implementation")) return false;
    module_items(b, l + 1);
    return true;
  }

}
