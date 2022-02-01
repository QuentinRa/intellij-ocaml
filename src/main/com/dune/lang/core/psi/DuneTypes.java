// This is a "generated" file. Not intended for manual editing.
package com.dune.lang.core.psi;

import com.intellij.psi.tree.IElementType;

public interface DuneTypes {

    // Composite element types

    DuneElementType FIELD = new DuneElement("Field");
    DuneElementType FIELDS = new DuneElement("Fields");
    DuneElementType STANZA = new DuneElement("Stanza");
    DuneElementType S_EXPR = new DuneElement("S-expr");
    DuneElementType VAR = new DuneElement("Var");

    // Token element types

    IElementType ATOM = new DuneTokenType("ATOM");
    IElementType COLON = new DuneTokenType(":");
    IElementType COMMENT = new DuneTokenType("COMMENT");
    IElementType EQUAL = new DuneTokenType("=");
    IElementType LPAREN = new DuneTokenType("(");
    IElementType RPAREN = new DuneTokenType(")");
    IElementType SHARP = new DuneTokenType("#");
    IElementType STRING = new DuneTokenType("STRING");
    IElementType VAR_END = new DuneTokenType("}");
    IElementType VAR_START = new DuneTokenType("%{");
}
