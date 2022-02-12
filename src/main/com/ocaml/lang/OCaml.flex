package com.ocaml.lang.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.or.lang.OCamlTypes;

import static com.intellij.psi.TokenType.*;

@SuppressWarnings("ALL")
%%

%{
    private OCamlTypes types;
    private int tokenStartIndex;
    private CharSequence quotedStringId;
    private int commentDepth;
    private boolean inComment = false;
    private boolean inAnnotationDetails = false;

    //Store the start index of a token
    private void tokenStart() {
        tokenStartIndex = zzStartRead;
    }

    //Set the start index of the token to the stored index
    private void tokenEnd() {
        zzStartRead = tokenStartIndex;
    }
%}

%class OCamlLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%eof{  return;
%eof}

EOL=\n|\r|\r\n
WHITE_SPACE_CHAR=[\ \t\f]|{EOL}
WHITE_SPACE={WHITE_SPACE_CHAR}+

NEWLINE=("\r"* "\n")
LOWERCASE=[a-z_]
UPPERCASE=[A-Z]
IDENTCHAR=[A-Za-z_0-9']

DECIMAL=[0-9]
DECIMAL_SEP=[0-9_]
HEXA=[0-9A-Fa-f]
HEXA_SEP=[0-9A-Fa-f_]
OCTAL=[0-7]
OCTAL_SEP=[0-7_]

DECIMAL_LITERAL={DECIMAL} {DECIMAL_SEP}*
HEXA_LITERAL="0" [xX] {HEXA} {HEXA_SEP}*
OCT_LITERAL="0" [oO] {OCTAL} {OCTAL_SEP}*
BIN_LITERAL="0" [bB] [0-1] [0-1_]*
INT_LITERAL= {DECIMAL_LITERAL} | {HEXA_LITERAL} | {OCT_LITERAL} | {BIN_LITERAL}
FLOAT_LITERAL={DECIMAL} {DECIMAL_SEP}* ("." {DECIMAL_SEP}* )? ([eE] [+-]? {DECIMAL} {DECIMAL_SEP}* )?
HEXA_FLOAT_LITERAL="0" [xX] {HEXA} {HEXA_SEP}* ("." {HEXA_SEP}* )? ([pP] [+-]? {DECIMAL} {DECIMAL_SEP}* )?
LITERAL_MODIFIER=[G-Zg-z]

ESCAPE_BACKSLASH="\\\\"
ESCAPE_SINGLE_QUOTE="\\'"
ESCAPE_LF="\\n"
ESCAPE_TAB="\\t"
ESCAPE_BACKSPACE="\\b"
ESCAPE_CR="\\r"
ESCAPE_QUOTE="\\\""
ESCAPE_DECIMAL="\\" {DECIMAL} {DECIMAL} {DECIMAL}
ESCAPE_HEXA="\\x" {HEXA} {HEXA}
ESCAPE_OCTAL="\\o" [0-3] {OCTAL} {OCTAL}
ESCAPE_CHAR= {ESCAPE_BACKSLASH} | {ESCAPE_SINGLE_QUOTE} | {ESCAPE_LF} | {ESCAPE_TAB} | {ESCAPE_BACKSPACE } | { ESCAPE_CR } | { ESCAPE_QUOTE } | {ESCAPE_DECIMAL} | {ESCAPE_HEXA} | {ESCAPE_OCTAL}

%state INITIAL
%state IN_STRING
%state IN_OCAML_ML_COMMENT
%state IN_OCAML_DOC_COMMENT
%state IN_OCAML_ANNOT

%%

<YYINITIAL>  {
      [^]   { yybegin(INITIAL); yypushback(1); }
}

<INITIAL> {
    {WHITE_SPACE} { return WHITE_SPACE; }

    "and"         { return OCamlTypes.AND; }
    "as"          { return OCamlTypes.AS; }
    "assert"      { return OCamlTypes.ASSERT; }
    "begin"       { return OCamlTypes.BEGIN; }
    "class"       { return OCamlTypes.CLASS; }
    "constraint"  { return OCamlTypes.CONSTRAINT; }
    "do"          { return OCamlTypes.DO; }
    "done"        { return OCamlTypes.DONE; }
    "downto"      { return OCamlTypes.DOWNTO; }
    "else"        { return OCamlTypes.ELSE; }
    "end"         { return OCamlTypes.END; }
    "endif"       { return OCamlTypes.ENDIF; }
    "exception"   { return OCamlTypes.EXCEPTION; }
    "external"    { return OCamlTypes.EXTERNAL; }
    "for"         { return OCamlTypes.FOR; }
    "fun"         { return OCamlTypes.FUN; }
    "function"    { return OCamlTypes.FUNCTION; }
    "functor"     { return OCamlTypes.FUNCTOR; }
    "if"          { return OCamlTypes.IF; }
    "in"          { return OCamlTypes.IN; }
    "include"     { return OCamlTypes.INCLUDE; }
    "inherit"     { return OCamlTypes.INHERIT; }
    "initializer" { return OCamlTypes.INITIALIZER; }
    "lazy"        { return OCamlTypes.LAZY; }
    "let"         { return OCamlTypes.LET; }
    "module"      { return OCamlTypes.MODULE;}
    "mutable"     { return OCamlTypes.MUTABLE; }
    "new"         { return OCamlTypes.NEW; }
    "nonrec"      { return OCamlTypes.NONREC; }
    "object"      { return OCamlTypes.OBJECT; }
    "of"          { return OCamlTypes.OF; }
    "open"        { return OCamlTypes.OPEN; }
    "or"          { return OCamlTypes.OR; }
    "pub"         { return OCamlTypes.PUB; }
    "pri"         { return OCamlTypes.PRI; }
    "rec"         { return OCamlTypes.REC; }
    "sig"         { return OCamlTypes.SIG; }
    "struct"      { return OCamlTypes.STRUCT; }
    "switch"      { return OCamlTypes.SWITCH; }
    "then"        { return OCamlTypes.THEN; }
    "to"          { return OCamlTypes.TO; }
    "try"         { return OCamlTypes.TRY; }
    "type"        { return OCamlTypes.TYPE; }
    "val"         { return OCamlTypes.VAL; }
    "virtual"     { return OCamlTypes.VIRTUAL; }
    "when"        { return OCamlTypes.WHEN; }
    "while"       { return OCamlTypes.WHILE; }
    "with"        { return OCamlTypes.WITH; }
    "raw"         { return OCamlTypes.RAW; }

    "mod"         { return OCamlTypes.MOD; }
    "land"        { return OCamlTypes.LAND; }
    "lor"         { return OCamlTypes.LOR; }
    "lxor"        { return OCamlTypes.LXOR; }
    "lsl"         { return OCamlTypes.LSL; }
    "lsr"         { return OCamlTypes.LSR; }
    "asr"         { return OCamlTypes.ASR; }

    "unit"        { return OCamlTypes.UNIT; }
    "ref"         { return OCamlTypes.REF; }
    "raise"       { return OCamlTypes.RAISE; }
    "method"      { return OCamlTypes.METHOD; }
    "private"     { return OCamlTypes.PRIVATE; }
    "match"       { return OCamlTypes.MATCH; }

    "option"    { return OCamlTypes.OPTION; }
    "None"      { return OCamlTypes.NONE; }
    "Some"      { return OCamlTypes.SOME; }

    "false"     { return OCamlTypes.BOOL_VALUE; }
    "true"      { return OCamlTypes.BOOL_VALUE; }

    "_"   { return OCamlTypes.UNDERSCORE; }

    "'" ( {ESCAPE_CHAR} | . ) "'"    { return OCamlTypes.CHAR_VALUE; }
    {LOWERCASE}{IDENTCHAR}*          { return OCamlTypes.LIDENT; }
    {UPPERCASE}{IDENTCHAR}*          { return OCamlTypes.UIDENT; }
    {INT_LITERAL}{LITERAL_MODIFIER}? { return OCamlTypes.INT_VALUE; }
    ({FLOAT_LITERAL} | {HEXA_FLOAT_LITERAL}){LITERAL_MODIFIER}? { return OCamlTypes.FLOAT_VALUE; }
    "'"{LOWERCASE}{IDENTCHAR}*       { return OCamlTypes.TYPE_ARGUMENT; }
    "`"{UPPERCASE}{IDENTCHAR}*       { return OCamlTypes.POLY_VARIANT; }
    "`"{LOWERCASE}{IDENTCHAR}*       { return OCamlTypes.POLY_VARIANT; }

    "\"" { if (!inComment) yybegin(IN_STRING); tokenStart(); }
    "(*" { yybegin(IN_OCAML_ML_COMMENT); commentDepth = 1; inComment = true; tokenStart(); }
    // not a normal, empty, comment
    // nor a (*** kind of comment
    "(**" [^*)] { yybegin(IN_OCAML_DOC_COMMENT); commentDepth = 1; inComment = true; tokenStart(); }
    // annotations
    "[@" { yybegin(IN_OCAML_ANNOT); inAnnotationDetails = false; tokenStart(); }

    "#if"     { return OCamlTypes.DIRECTIVE_IF; }
    "#else"   { return OCamlTypes.DIRECTIVE_ELSE; }
    "#elif"   { return OCamlTypes.DIRECTIVE_ELIF; }
    "#endif"  { return OCamlTypes.DIRECTIVE_ENDIF; }
    "#end"    { return OCamlTypes.DIRECTIVE_END; }

    "##"  { return OCamlTypes.SHARPSHARP; }
    "@@"  { return OCamlTypes.AT_SIGN_2; }
    "@@@" { return OCamlTypes.AT_SIGN_3; }

    "::"  { return OCamlTypes.SHORTCUT; }
    "=>"  { return OCamlTypes.ARROW; }
    "->"  { return OCamlTypes.RIGHT_ARROW; }
    "<-"  { return OCamlTypes.LEFT_ARROW; }
    "|>"  { return OCamlTypes.PIPE_FORWARD; }
    "</"  { return OCamlTypes.TAG_LT_SLASH; }
    "/>"  { return OCamlTypes.TAG_AUTO_CLOSE; }
    "[|"  { return OCamlTypes.LARRAY; }
    "|]"  { return OCamlTypes.RARRAY; }

    "===" { return OCamlTypes.EQEQEQ; }
    "=="  { return OCamlTypes.EQEQ; }
    "="   { return OCamlTypes.EQ; }
    "!==" { return OCamlTypes.NOT_EQEQ; }
    "!="  { return OCamlTypes.NOT_EQ; }
    ":="  { return OCamlTypes.COLON_EQ; }
    ":>"  { return OCamlTypes.COLON_GT; }
    "<="  { return OCamlTypes.LT_OR_EQUAL; }
    ">="  { return OCamlTypes.GT_OR_EQUAL; }
    ";;"  { return OCamlTypes.SEMISEMI; }
    "||"  { return OCamlTypes.L_OR; }
    "&&"  { return OCamlTypes.L_AND; }
    "<>"  { return OCamlTypes.OP_STRUCT_DIFF; }

    ","   { return OCamlTypes.COMMA; }
    ":"   { return OCamlTypes.COLON; }
    ";"   { return OCamlTypes.SEMI; }
    "'"   { return OCamlTypes.SINGLE_QUOTE; }
    "..." { return OCamlTypes.DOTDOTDOT; }
    ".."  { return OCamlTypes.DOTDOT; }
    "."   { return OCamlTypes.DOT; }
    "|"   { return OCamlTypes.PIPE; }
    "("   { return OCamlTypes.LPAREN; }
    ")"   { return OCamlTypes.RPAREN; }
    "{"   { return OCamlTypes.LBRACE; }
    "}"   { return OCamlTypes.RBRACE; }
    "["   { return OCamlTypes.LBRACKET; }
    "]"   { return OCamlTypes.RBRACKET; }
    "@"   { return OCamlTypes.AT_SIGN; }
    "#"   { return OCamlTypes.SHARP; }
    "?"   { return OCamlTypes.QUESTION_MARK; }
    "!"   { return OCamlTypes.EXCLAMATION_MARK; }
    "$"   { return OCamlTypes.DOLLAR; }
    "`"   { return OCamlTypes.BACKTICK; }
    "~"   { return OCamlTypes.TILDE; }
    "&"   { return OCamlTypes.AMPERSAND; }

    "<"  { return OCamlTypes.LT; }
    ">"  { return OCamlTypes.GT; }

    "\^"  { return OCamlTypes.CARET; }
    "+."  { return OCamlTypes.PLUSDOT; }
    "-."  { return OCamlTypes.MINUSDOT; }
    "/."  { return OCamlTypes.SLASHDOT; }
    "*."  { return OCamlTypes.STARDOT; }
    "+"   { return OCamlTypes.PLUS; }
    "-"   { return OCamlTypes.MINUS; }
    "/"   { return OCamlTypes.SLASH; }
    "*"   { return OCamlTypes.STAR; }
    "%"   { return OCamlTypes.PERCENT; }
}

<IN_STRING> {
    "\"" { yybegin(INITIAL); tokenEnd(); return OCamlTypes.STRING_VALUE; }
    "\\" { NEWLINE } ([ \t] *) { }
    "\\" [\\\'\"ntbr ] { }
    "\\" [0-9] [0-9] [0-9] { }
    "\\" "o" [0-3] [0-7] [0-7] { }
    "\\" "x" [0-9a-fA-F] [0-9a-fA-F] { }
    "\\" . { }
    { NEWLINE } { }
    . { }
    <<EOF>> { yybegin(INITIAL); tokenEnd(); return OCamlTypes.STRING_VALUE; }
}

<IN_OCAML_ML_COMMENT> {
    "(*" { commentDepth += 1; }
    "*)" { commentDepth -= 1; if (commentDepth == 0) { inComment = false; yybegin(INITIAL); tokenEnd(); return OCamlTypes.MULTI_COMMENT; } }
     . | {NEWLINE} { }
     <<EOF>> { yybegin(INITIAL); tokenEnd(); return OCamlTypes.MULTI_COMMENT; }
}

<IN_OCAML_DOC_COMMENT> {
    "(*" { commentDepth += 1; }
    "*)" { commentDepth -= 1; if (commentDepth == 0) { inComment = false; yybegin(INITIAL); tokenEnd(); return OCamlTypes.DOC_COMMENT; } }
     . | {NEWLINE} { }
     <<EOF>> { yybegin(INITIAL); tokenEnd(); return OCamlTypes.DOC_COMMENT; }
}

<IN_OCAML_ANNOT> {
    "]" { if (!inAnnotationDetails) { yybegin(INITIAL); tokenEnd(); return OCamlTypes.ANNOTATION; } }
    "{|" { inAnnotationDetails = true; }
    "|}" { inAnnotationDetails = false; }
     . | {NEWLINE} { }
     <<EOF>> { yybegin(INITIAL); tokenEnd(); return OCamlTypes.ANNOTATION; }
}

[^] { return BAD_CHARACTER; }