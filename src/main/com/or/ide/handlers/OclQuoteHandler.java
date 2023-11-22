package com.or.ide.handlers;

import com.intellij.codeInsight.editorActions.SimpleTokenSetQuoteHandler;
import com.intellij.psi.TokenType;
import com.or.lang.OCamlTypes;

public class OclQuoteHandler extends SimpleTokenSetQuoteHandler {

    public OclQuoteHandler() {
        super(OCamlTypes.STRING_VALUE, OCamlTypes.DOUBLE_QUOTE, TokenType.BAD_CHARACTER);
    }
}
