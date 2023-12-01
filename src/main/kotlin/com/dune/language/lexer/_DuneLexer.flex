package com.dune.language.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
//import static com.intellij.psi.TokenType.WHITE_SPACE;
import static com.dune.language.psi.DuneTypes.*;

%%

%{
  private int tokenStartIndex;

  public _DuneLexer() {
    this((java.io.Reader)null);
  }

  // Store the start index of a token
  private void tokenStart() {
    tokenStartIndex = zzStartRead;
  }

  // Set the start index of the token to the stored index
  private void tokenEnd() {
    zzStartRead = tokenStartIndex;
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
NEWLINE=("\r"* "\n")

%state WAITING_VALUE
%state INITIAL
%state IN_STRING

%%
<YYINITIAL>  {
      [^]   { yybegin(INITIAL); yypushback(1); }
}

<INITIAL> {
  {WHITE_SPACE}        { return WHITE_SPACE; }

  "("                  { return LPAREN; }
  ")"                  { return RPAREN; }
  ";" [^\n]*           { return COMMENT; }
  [^ \t\n\r()\";]+     { return ATOM; }
  "\""                 { yybegin(IN_STRING); tokenStart(); }
}

// https://dune.readthedocs.io/en/stable/reference/lexical-conventions.html
<IN_STRING> {
    "\"" { yybegin(INITIAL); tokenEnd(); return STRING; }
    "\\" { NEWLINE } ([ \t] *) { }
    "\\" [\\\'\"ntbr ] { }
    "\\" [0-9] [0-9] [0-9] { }
    "\\" "o" [0-3] [0-7] [0-7] { }
    "\\" "x" [0-9a-fA-F] [0-9a-fA-F] { }
    "\\" . { }
    { NEWLINE } { }
    . { }
    <<EOF>> { yybegin(INITIAL); tokenEnd(); return STRING; }
}

[^] { return BAD_CHARACTER; }
