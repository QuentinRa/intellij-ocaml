package com.dune.lang.lexer;

import com.intellij.lexer.FlexAdapter;

public class DuneLexerAdapter extends FlexAdapter {

    public DuneLexerAdapter() {
        super(new DuneLexer(null));
    }
}
