package com.ocaml.language.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static com.ocaml.language.psi.OCamlTypes.*;

%%

%{
  public _OCamlLexer() {
    this((java.io.Reader)null);
  }
%}

%public
%class _OCamlLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

EOL=\R
WHITE_SPACE=\s+


%%
<YYINITIAL> {
  {WHITE_SPACE}       { return WHITE_SPACE; }



}

[^] { return BAD_CHARACTER; }
