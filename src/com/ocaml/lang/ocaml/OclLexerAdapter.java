package com.ocaml.lang.ocaml;

import com.intellij.lexer.*;

public class OclLexerAdapter extends FlexAdapter {

    public OclLexerAdapter() {
        super(new OclLexer(null));
    }
}