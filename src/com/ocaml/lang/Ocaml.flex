package com.ocaml.lang.ocaml;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.ocaml.lang.core.psi.OclTypes;

import static com.intellij.psi.TokenType.*;

@SuppressWarnings("ALL")
%%

%{
    private OclTypes types;
    private int tokenStartIndex;
    private CharSequence quotedStringId;
    private int commentDepth;
    private boolean inComment = false;

    //Store the start index of a token
    private void tokenStart() {
        tokenStartIndex = zzStartRead;
    }

    //Set the start index of the token to the stored index
    private void tokenEnd() {
        zzStartRead = tokenStartIndex;
    }
%}

%class OclLexer
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

%%

<YYINITIAL>  {
      [^]   { yybegin(INITIAL); yypushback(1); }
}

<INITIAL> {
    {WHITE_SPACE} { return WHITE_SPACE; }

    "and"         { return OclTypes.AND; }
    "as"          { return OclTypes.AS; }
    "assert"      { return OclTypes.ASSERT; }
    "begin"       { return OclTypes.BEGIN; }
    "class"       { return OclTypes.CLASS; }
    "constraint"  { return OclTypes.CONSTRAINT; }
    "do"          { return OclTypes.DO; }
    "done"        { return OclTypes.DONE; }
    "downto"      { return OclTypes.DOWNTO; }
    "else"        { return OclTypes.ELSE; }
    "end"         { return OclTypes.END; }
    "endif"       { return OclTypes.ENDIF; }
    "exception"   { return OclTypes.EXCEPTION; }
    "external"    { return OclTypes.EXTERNAL; }
    "for"         { return OclTypes.FOR; }
    "fun"         { return OclTypes.FUN; }
    "function"    { return OclTypes.FUNCTION; }
    "functor"     { return OclTypes.FUNCTOR; }
    "if"          { return OclTypes.IF; }
    "in"          { return OclTypes.IN; }
    "include"     { return OclTypes.INCLUDE; }
    "inherit"     { return OclTypes.INHERIT; }
    "initializer" { return OclTypes.INITIALIZER; }
    "lazy"        { return OclTypes.LAZY; }
    "let"         { return OclTypes.LET; }
    "module"      { return OclTypes.MODULE;}
    "mutable"     { return OclTypes.MUTABLE; }
    "new"         { return OclTypes.NEW; }
    "nonrec"      { return OclTypes.NONREC; }
    "object"      { return OclTypes.OBJECT; }
    "of"          { return OclTypes.OF; }
    "open"        { return OclTypes.OPEN; }
    "or"          { return OclTypes.OR; }
    "pub"         { return OclTypes.PUB; }
    "pri"         { return OclTypes.PRI; }
    "rec"         { return OclTypes.REC; }
    "sig"         { return OclTypes.SIG; }
    "struct"      { return OclTypes.STRUCT; }
    "switch"      { return OclTypes.SWITCH; }
    "then"        { return OclTypes.THEN; }
    "to"          { return OclTypes.TO; }
    "try"         { return OclTypes.TRY; }
    "type"        { return OclTypes.TYPE; }
    "val"         { return OclTypes.VAL; }
    "virtual"     { return OclTypes.VIRTUAL; }
    "when"        { return OclTypes.WHEN; }
    "while"       { return OclTypes.WHILE; }
    "with"        { return OclTypes.WITH; }
    "raw"         { return OclTypes.RAW; }

    "mod"         { return OclTypes.MOD; }
    "land"        { return OclTypes.LAND; }
    "lor"         { return OclTypes.LOR; }
    "lxor"        { return OclTypes.LXOR; }
    "lsl"         { return OclTypes.LSL; }
    "lsr"         { return OclTypes.LSR; }
    "asr"         { return OclTypes.ASR; }

    "unit"        { return OclTypes.UNIT; }
    "ref"         { return OclTypes.REF; }
    "raise"       { return OclTypes.RAISE; }
    "method"      { return OclTypes.METHOD; }
    "private"     { return OclTypes.PRIVATE; }
    "match"       { return OclTypes.MATCH; }

    "option"    { return OclTypes.OPTION; }
    "None"      { return OclTypes.NONE; }
    "Some"      { return OclTypes.SOME; }

    "false"     { return OclTypes.BOOL_VALUE; }
    "true"      { return OclTypes.BOOL_VALUE; }

    "_"   { return OclTypes.UNDERSCORE; }

    "'" ( {ESCAPE_CHAR} | . ) "'"    { return OclTypes.CHAR_VALUE; }
    {LOWERCASE}{IDENTCHAR}*          { return OclTypes.LIDENT; }
    {UPPERCASE}{IDENTCHAR}*          { return OclTypes.UIDENT; }
    {INT_LITERAL}{LITERAL_MODIFIER}? { return OclTypes.INT_VALUE; }
    ({FLOAT_LITERAL} | {HEXA_FLOAT_LITERAL}){LITERAL_MODIFIER}? { return OclTypes.FLOAT_VALUE; }
    "'"{LOWERCASE}{IDENTCHAR}*       { return OclTypes.TYPE_ARGUMENT; }
    "`"{UPPERCASE}{IDENTCHAR}*       { return OclTypes.POLY_VARIANT; }
    "`"{LOWERCASE}{IDENTCHAR}*       { return OclTypes.POLY_VARIANT; }
    "[@@" .* "]"                     { return OclTypes.ANNOTATION; }

    "\"" { if (!inComment) yybegin(IN_STRING); tokenStart(); }
    "(*" { yybegin(IN_OCAML_ML_COMMENT); commentDepth = 1; inComment = true; tokenStart(); }

    "#if"     { return OclTypes.DIRECTIVE_IF; }
    "#else"   { return OclTypes.DIRECTIVE_ELSE; }
    "#elif"   { return OclTypes.DIRECTIVE_ELIF; }
    "#endif"  { return OclTypes.DIRECTIVE_ENDIF; }
    "#end"    { return OclTypes.DIRECTIVE_END; }

    "##"  { return OclTypes.SHARPSHARP; }
    "@@"  { return OclTypes.ARROBASE_2; }
    "@@@" { return OclTypes.ARROBASE_3; }

    "::"  { return OclTypes.SHORTCUT; }
    "=>"  { return OclTypes.ARROW; }
    "->"  { return OclTypes.RIGHT_ARROW; }
    "<-"  { return OclTypes.LEFT_ARROW; }
    "|>"  { return OclTypes.PIPE_FORWARD; }
    "</"  { return OclTypes.TAG_LT_SLASH; }
    "/>"  { return OclTypes.TAG_AUTO_CLOSE; }
    "[|"  { return OclTypes.LARRAY; }
    "|]"  { return OclTypes.RARRAY; }

    "===" { return OclTypes.EQEQEQ; }
    "=="  { return OclTypes.EQEQ; }
    "="   { return OclTypes.EQ; }
    "!==" { return OclTypes.NOT_EQEQ; }
    "!="  { return OclTypes.NOT_EQ; }
    ":="  { return OclTypes.COLON_EQ; }
    ":>"  { return OclTypes.COLON_GT; }
    "<="  { return OclTypes.LT_OR_EQUAL; }
    ">="  { return OclTypes.GT_OR_EQUAL; }
    ";;"  { return OclTypes.SEMISEMI; }
    "||"  { return OclTypes.L_OR; }
    "&&"  { return OclTypes.L_AND; }
    "<>"  { return OclTypes.OP_STRUCT_DIFF; }

    ","   { return OclTypes.COMMA; }
    ":"   { return OclTypes.COLON; }
    ";"   { return OclTypes.SEMI; }
    "'"   { return OclTypes.SINGLE_QUOTE; }
    "..." { return OclTypes.DOTDOTDOT; }
    ".."  { return OclTypes.DOTDOT; }
    "."   { return OclTypes.DOT; }
    "|"   { return OclTypes.PIPE; }
    "("   { return OclTypes.LPAREN; }
    ")"   { return OclTypes.RPAREN; }
    "{"   { return OclTypes.LBRACE; }
    "}"   { return OclTypes.RBRACE; }
    "["   { return OclTypes.LBRACKET; }
    "]"   { return OclTypes.RBRACKET; }
    "@"   { return OclTypes.ARROBASE; }
    "#"   { return OclTypes.SHARP; }
    "?"   { return OclTypes.QUESTION_MARK; }
    "!"   { return OclTypes.EXCLAMATION_MARK; }
    "$"   { return OclTypes.DOLLAR; }
    "`"   { return OclTypes.BACKTICK; }
    "~"   { return OclTypes.TILDE; }
    "&"   { return OclTypes.AMPERSAND; }

    "<"  { return OclTypes.LT; }
    ">"  { return OclTypes.GT; }

    "\^"  { return OclTypes.CARRET; }
    "+."  { return OclTypes.PLUSDOT; }
    "-."  { return OclTypes.MINUSDOT; }
    "/."  { return OclTypes.SLASHDOT; }
    "*."  { return OclTypes.STARDOT; }
    "+"   { return OclTypes.PLUS; }
    "-"   { return OclTypes.MINUS; }
    "/"   { return OclTypes.SLASH; }
    "*"   { return OclTypes.STAR; }
    "%"   { return OclTypes.PERCENT; }
}

<IN_STRING> {
    "\"" { yybegin(INITIAL); tokenEnd(); return OclTypes.STRING_VALUE; }
    "\\" { NEWLINE } ([ \t] *) { }
    "\\" [\\\'\"ntbr ] { }
    "\\" [0-9] [0-9] [0-9] { }
    "\\" "o" [0-3] [0-7] [0-7] { }
    "\\" "x" [0-9a-fA-F] [0-9a-fA-F] { }
    "\\" . { }
    { NEWLINE } { }
    . { }
    <<EOF>> { yybegin(INITIAL); tokenEnd(); return OclTypes.STRING_VALUE; }
}

<IN_OCAML_ML_COMMENT> {
    "(*" { commentDepth += 1; }
    "*)" { commentDepth -= 1; if (commentDepth == 0) { inComment = false; yybegin(INITIAL); tokenEnd(); return OclTypes.MULTI_COMMENT; } }
     . | {NEWLINE} { }
     <<EOF>> { yybegin(INITIAL); tokenEnd(); return OclTypes.MULTI_COMMENT; }
}

[^] { return BAD_CHARACTER; }