package com.odoc.lang;

import com.intellij.psi.tree.IElementType;
import com.odoc.OdocLanguage;

public interface OdocTypes {
    IElementType ATOM = new IElementType("ATOM", OdocLanguage.INSTANCE);
    IElementType BOLD = new IElementType("BOLD", OdocLanguage.INSTANCE);
    IElementType CODE = new IElementType("CODE", OdocLanguage.INSTANCE);
    IElementType COLON = new IElementType("COLON", OdocLanguage.INSTANCE);
    IElementType CROSS_REF = new IElementType("CROSS_REF", OdocLanguage.INSTANCE);
    IElementType EMPHASIS = new IElementType("EMPHASIS", OdocLanguage.INSTANCE);
    IElementType ITALIC = new IElementType("ITALIC", OdocLanguage.INSTANCE);
    IElementType LINK_START = new IElementType("LINK_START", OdocLanguage.INSTANCE);
    IElementType LIST_ITEM_START = new IElementType("LIST_ITEM_START", OdocLanguage.INSTANCE);
    IElementType NEW_LINE = new IElementType("NEW_LINE", OdocLanguage.INSTANCE);
    IElementType COMMENT_START = new IElementType("COMMENT_START", OdocLanguage.INSTANCE);
    IElementType COMMENT_END = new IElementType("COMMENT_END", OdocLanguage.INSTANCE);
    IElementType O_LIST = new IElementType("O_LIST", OdocLanguage.INSTANCE);
    IElementType PRE = new IElementType("PRE", OdocLanguage.INSTANCE);
    IElementType RBRACE = new IElementType("RBRACE", OdocLanguage.INSTANCE);
    IElementType SECTION = new IElementType("SECTION", OdocLanguage.INSTANCE);
    IElementType TAG = new IElementType("TAG", OdocLanguage.INSTANCE);
    IElementType U_LIST = new IElementType("U_LIST", OdocLanguage.INSTANCE);
}
