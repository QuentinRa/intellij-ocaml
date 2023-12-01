package com.dune.language.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static com.dune.language.psi.DuneTypes.*;

%%

%{
  public _DuneLexer() {
    this((java.io.Reader)null);
  }
%}

%public
%class _DuneLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

EOL=\R
WHITE_SPACE=\s+


%%
<YYINITIAL> {
  {WHITE_SPACE}        { return WHITE_SPACE; }

  ";" [^\n]*           { return COMMENT; }
  [^ \t\n\r()\";]+     { return ATOM_VALUE; }
  "STRING_VALUE"       { return STRING_VALUE; }
}

[^] { return BAD_CHARACTER; }
