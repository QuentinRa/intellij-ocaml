// This is a "generated" file. Not intended for manual editing.
package com.dune.lang.core.psi.impl;

import com.dune.lang.core.psi.DuneTokenType;
import com.intellij.psi.tree.IElementType;

public interface DuneTypes {

    // Composite element types

    IElementType FIELD = new DuneTokenType("Field");
    IElementType FIELDS = new DuneTokenType("Fields");
    IElementType STANZA = new DuneTokenType("Stanza");
    IElementType S_EXPR = new DuneTokenType("S-expr");
    IElementType VAR = new DuneTokenType("Var");

    // Token element types

    IElementType ATOM = new DuneTokenType("ATOM");
    IElementType COLON = new DuneTokenType("COLON");
    IElementType COMMENT = new DuneTokenType("COMMENT");
    IElementType EQUAL = new DuneTokenType("EQUAL");
    IElementType LPAREN = new DuneTokenType("LPAREN");
    IElementType RPAREN = new DuneTokenType("RPAREN");
    IElementType SHARP = new DuneTokenType("SHARP");
    IElementType STRING = new DuneTokenType("String");
    IElementType VAR_END = new DuneTokenType("VAR_END");
    IElementType VAR_START = new DuneTokenType("VAR_START");
}
