package com.ocaml.lang.lexer;

import com.intellij.lexer.FlexAdapter;

public class OCamlLexerAdapter extends FlexAdapter {

    public OCamlLexerAdapter() {
        super(new OCamlLexer(null));
    }
}
