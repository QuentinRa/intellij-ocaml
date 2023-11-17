package com.ocaml.language.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static com.intellij.psi.TokenType.ERROR_ELEMENT;
import static com.ocaml.language.psi.OCamlTypes.*;

%%

%{
  private int tokenStartIndex;
  private CharSequence quotedStringId;
  private int commentDepth;
  private boolean inCommentString = false;
  private boolean inAnnotationDetails = false;

  //Store the start index of a token
  private void tokenStart() {
    tokenStartIndex = zzStartRead;
  }

  //Set the start index of the token to the stored index
  private void tokenEnd() {
    zzStartRead = tokenStartIndex;
  }

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
NEWLINE=("\r"* "\n")

// basic
UNDERSCORE="_"
DIGIT=[0-9]
DIGIT_7=[0-7]
DIGIT_3=[0-3]
DIGIT_1=[0-1]
HEXA_LETTER=[A-Fa-f]
LOWERCASE=[a-z]
UPPERCASE=[A-Z]
IDENTCHAR=[A-Za-z_0-9']

// aliases
DIGIT_UNDERSCORE=({DIGIT}|{UNDERSCORE})
DIGIT_HEXA=({DIGIT}|{HEXA_LETTER})
DIGIT_HEXA_UNDERSCORE=({DIGIT_HEXA}|{UNDERSCORE})
DIGIT_1_UNDERSCORE=({DIGIT_1}|{UNDERSCORE})
DIGIT_7_UNDERSCORE=({DIGIT_7}|{UNDERSCORE})

%state WAITING_VALUE
%state INITIAL
%state IN_STRING
%state IN_QUOTED_STRING
%state IN_OCAML_ML_COMMENT
%state IN_OCAML_DOC_COMMENT
%state IN_OCAML_ANNOT

%%
<YYINITIAL>  {
      [^]                             { yybegin(INITIAL); yypushback(1); }
}

<INITIAL> {
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

  "\"" { yybegin(IN_STRING); tokenStart(); }

  // https://v2.ocaml.org/releases/4.14/htmlman/lex.html#sss:floating-point-literals
  {DIGIT} {DIGIT_UNDERSCORE}* "." {DIGIT_UNDERSCORE} ("e"|"E") ("+"|"-") {DIGIT} {DIGIT_UNDERSCORE}* { return FLOAT_VALUE; }
  {DIGIT} {DIGIT_UNDERSCORE}* "." {DIGIT_UNDERSCORE} { return FLOAT_VALUE; }
  ("0x"|"0X") {DIGIT_HEXA} {DIGIT_HEXA_UNDERSCORE} "." {DIGIT_HEXA_UNDERSCORE} ("p"|"P")
    ("+"|"-") {DIGIT} {DIGIT_UNDERSCORE} { return FLOAT_VALUE; }
  ("0x"|"0X") {DIGIT_HEXA} {DIGIT_HEXA_UNDERSCORE} "." {DIGIT_HEXA_UNDERSCORE} { return FLOAT_VALUE; }

  // https://v2.ocaml.org/releases/4.14/htmlman/lex.html#sss:integer-literals
  {DIGIT} {DIGIT_UNDERSCORE}* { return INTEGER_VALUE; }
  ("0x"|"0X") {DIGIT_HEXA} {DIGIT_HEXA_UNDERSCORE}* { return INTEGER_VALUE; }
  ("0o"|"0O") {DIGIT_7} {DIGIT_7_UNDERSCORE}* { return INTEGER_VALUE; }
  ("0b"|"0B") {DIGIT_1} {DIGIT_1_UNDERSCORE}* { return INTEGER_VALUE; }

  // https://v2.ocaml.org/releases/4.14/htmlman/lex.html#sss:character-literals
  "'" .  "'" { return CHAR_VALUE; }
  "'" [\\] ([\\\"'nbtr]|space)  "'" { return CHAR_VALUE; }
  "'" [\\] {DIGIT} {DIGIT} {DIGIT}  "'" { return CHAR_VALUE; }
  "'" [\\] "x" {DIGIT_HEXA} {DIGIT_HEXA} {DIGIT_HEXA}  "'" { return CHAR_VALUE; }
  "'" [\\] "o" {DIGIT_3} {DIGIT_7} {DIGIT_7}  "'" { return CHAR_VALUE; }

  // https://v2.ocaml.org/releases/4.14/htmlman/lex.html#sss:lex:identifiers
  ({ LOWERCASE } | "_") { IDENTCHAR } * { return LOWERCASE_IDENT; }
  { UPPERCASE } { IDENTCHAR } * { return CAPITALIZED_IDENT; }
  ({ UPPERCASE } | { LOWERCASE } | "_") { IDENTCHAR } * { return IDENT; }

  // todo: missing quoted strings

  // todo: these are not valid yet
  "(*" { yybegin(IN_OCAML_ML_COMMENT); inCommentString = false; commentDepth = 1; tokenStart(); }
  // not a normal, empty, comment nor a (*** kind of comment
  "(**" [^*)] { yybegin(IN_OCAML_DOC_COMMENT); inCommentString = false; commentDepth = 1; tokenStart(); }
  // annotations
  "[@" { yybegin(IN_OCAML_ANNOT); inAnnotationDetails = false; tokenStart(); }
}

// https://v2.ocaml.org/releases/4.14/htmlman/lex.html#sss:stringliterals
<IN_STRING> {
    "\"" { yybegin(INITIAL); tokenEnd(); return STRING_VALUE; }
    [\\] "u{" {DIGIT_HEXA}* "}" { }
    [\\] "newline" ("space"|"tab") { }
    [\\] "newline" { }
    [\\] ([\\\"'nbtr]|space) { }
    [\\] {DIGIT} {DIGIT} {DIGIT} { }
    [\\] "x" {DIGIT_HEXA} {DIGIT_HEXA} {DIGIT_HEXA} { }
    [\\] "o" {DIGIT_3} {DIGIT_7} {DIGIT_7} { }
    <<EOF>> { yybegin(INITIAL); tokenEnd(); return STRING_VALUE; }
}

// (*(**)*) is valid, while (*(**) is not
// (*"*)"*) is valid, while (*"*) or (**)*) are not
<IN_OCAML_ML_COMMENT> {
    "(*" { if (!inCommentString) commentDepth += 1; }
    "*)" { if (!inCommentString) { commentDepth -= 1; if(commentDepth == 0) { yybegin(INITIAL); tokenEnd(); return COMMENT; } } }
    "\"" { inCommentString = !inCommentString; }
     . | {NEWLINE} { }
    <<EOF>> { yybegin(INITIAL); tokenEnd(); return ERROR_ELEMENT; }
}

<IN_OCAML_DOC_COMMENT> {
    "(*" { if (!inCommentString) commentDepth += 1; }
    "*)" { if (!inCommentString) { commentDepth -= 1; if (commentDepth == 0) { yybegin(INITIAL); tokenEnd(); return DOC_COMMENT; } } }
    "\"" { inCommentString = !inCommentString; }
     . | {NEWLINE} { }
     <<EOF>> { yybegin(INITIAL); tokenEnd(); return DOC_COMMENT; }
}

<IN_OCAML_ANNOT> {
    "]" { if (!inAnnotationDetails) { yybegin(INITIAL); tokenEnd(); return ANNOTATION; } }
    "{|" { inAnnotationDetails = true; }
    "|}" { inAnnotationDetails = false; }
     . | {NEWLINE} { }
     <<EOF>> { yybegin(INITIAL); tokenEnd(); return ANNOTATION; }
}

[^] { return BAD_CHARACTER; }
