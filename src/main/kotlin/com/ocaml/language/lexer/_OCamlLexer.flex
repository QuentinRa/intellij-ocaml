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

  "and"               { return AND; }
  "as"                { return AS; }
  "assert"            { return ASSERT; }
  "begin"             { return BEGIN; }
  "class"             { return CLASS; }
  "constraint"        { return CONSTRAINT; }
  "do"                { return DO; }
  "done"              { return DONE; }
  "downto"            { return DOWNTO; }
  "else"              { return ELSE; }
  "end"               { return END; }
  "exception"         { return EXCEPTION; }
  "external"          { return EXTERNAL; }
  "for"               { return FOR; }
  "fun"               { return FUN; }
  "function"          { return FUNCTION; }
  "functor"           { return FUNCTOR; }
  "if"                { return IF; }
  "in"                { return IN; }
  "include"           { return INCLUDE; }
  "inherit"           { return INHERIT; }
  "initializer"       { return INITIALIZER; }
  "lazy"              { return LAZY; }
  "let"               { return LET; }
  "module"            { return MODULE; }
  "mutable"           { return MUTABLE; }
  "new"               { return NEW; }
  "nonrec"            { return NONREC; }
  "object"            { return OBJECT; }
  "of"                { return OF; }
  "open"              { return OPEN; }
  "or"                { return OR; }
  "rec"               { return REC; }
  "sig"               { return SIG; }
  "struct"            { return STRUCT; }
  "then"              { return THEN; }
  "to"                { return TO; }
  "try"               { return TRY; }
  "type"              { return TYPE; }
  "val"               { return VAL; }
  "virtual"           { return VIRTUAL; }
  "when"              { return WHEN; }
  "while"             { return WHILE; }
  "with"              { return WITH; }
  "{<"                { return LBRACELESS; }
  ">}"                { return GREATERRBRACE; }
  "mod"               { return MOD; }
  "land"              { return LAND; }
  "lor"               { return LOR; }
  "lxor"              { return LXOR; }
  "lsl"               { return LSL; }
  "lsr"               { return LSR; }
  "asr"               { return ASR; }
  "true"              { return TRUE; }
  "false"             { return FALSE; }
  "method"            { return METHOD; }
  "private"           { return PRIVATE; }
  "match"             { return MATCH; }
  "_"                 { return UNDERSCORE; }
  "::"                { return SHORTCUT; }
  "->"                { return RIGHT_ARROW; }
  "<-"                { return LEFT_ARROW; }
  "[|"                { return LARRAY; }
  "|]"                { return RARRAY; }
  "="                 { return EQ; }
  "!="                { return NOT_EQ; }
  ":="                { return COLON_EQ; }
  ":>"                { return COLON_GT; }
  ";;"                { return SEMISEMI; }
  "||"                { return L_OR; }
  "&&"                { return L_AND; }
  "^"                 { return CARRET; }
  "-."                { return MINUSDOT; }
  "+"                 { return PLUS; }
  "-"                 { return MINUS; }
  "/"                 { return SLASH; }
  "*"                 { return STAR; }
  ","                 { return COMMA; }
  ":"                 { return COLON; }
  ";"                 { return SEMI; }
  "'"                 { return SINGLE_QUOTE; }
  ".."                { return DOTDOT; }
  "."                 { return DOT; }
  "|"                 { return PIPE; }
  "("                 { return LPAREN; }
  ")"                 { return RPAREN; }
  "{"                 { return LBRACE; }
  "}"                 { return RBRACE; }
  "["                 { return LBRACKET; }
  "]"                 { return RBRACKET; }
  "@"                 { return ARROBASE; }
  "#"                 { return SHARP; }
  "?"                 { return QUESTION_MARK; }
  "!"                 { return EXCLAMATION_MARK; }
  "$"                 { return DOLLAR; }
  "`"                 { return BACKTICK; }
  "~"                 { return TILDE; }
  "&"                 { return AMPERSAND; }
  "%"                 { return PERCENT; }
  "<"                 { return LT; }
  ">"                 { return GT; }
  "[>"                { return LBRACKETGREATER; }
  "[<"                { return LBRACKETLESS; }


}

[^] { return BAD_CHARACTER; }
