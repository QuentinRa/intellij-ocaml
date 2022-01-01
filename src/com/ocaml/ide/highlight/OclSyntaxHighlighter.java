package com.ocaml.ide.highlight;

import com.intellij.lexer.*;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.editor.colors.*;
import com.intellij.openapi.fileTypes.*;
import com.intellij.psi.tree.*;
import com.ocaml.lang.core.psi.*;
import com.ocaml.lang.ocaml.*;
import org.jetbrains.annotations.*;

import java.util.*;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;
import static com.intellij.psi.TokenType.BAD_CHARACTER;

public class OclSyntaxHighlighter implements SyntaxHighlighter {
    private static final TextAttributesKey TYPE_ARGUMENT_KEY = TextAttributesKey.createTextAttributesKey("TYPE_ARGUMENT");

    public static final TextAttributesKey BRACES_ = createTextAttributesKey("OCAML_BRACES", DefaultLanguageHighlighterColors.BRACES);
    public static final TextAttributesKey BRACKETS_ = createTextAttributesKey("OCAML_BRACKETS", DefaultLanguageHighlighterColors.BRACKETS);
    public static final TextAttributesKey KEYWORD_ = createTextAttributesKey("OCAML_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD);
    public static final TextAttributesKey NUMBER_ = createTextAttributesKey("OCAML_NUMBER", DefaultLanguageHighlighterColors.NUMBER);
    public static final TextAttributesKey OPERATION_SIGN_ = createTextAttributesKey("OCAML_OPERATION_SIGN", DefaultLanguageHighlighterColors.OPERATION_SIGN);
    public static final TextAttributesKey OPTION_ = createTextAttributesKey("OCAML_OPTION", DefaultLanguageHighlighterColors.STATIC_FIELD);
    public static final TextAttributesKey PARENS_ = createTextAttributesKey("OCAML_PARENS", DefaultLanguageHighlighterColors.PARENTHESES);
    public static final TextAttributesKey POLY_VARIANT_ = createTextAttributesKey("OCAML_POLY_VARIANT", DefaultLanguageHighlighterColors.STATIC_FIELD);
    public static final TextAttributesKey RML_COMMENT_ = createTextAttributesKey("OCAML_COMMENT", DefaultLanguageHighlighterColors.BLOCK_COMMENT);
    public static final TextAttributesKey SEMICOLON_ = createTextAttributesKey("OCAML_SEMICOLON", DefaultLanguageHighlighterColors.SEMICOLON);
    public static final TextAttributesKey STRING_ = createTextAttributesKey("OCAML_STRING", DefaultLanguageHighlighterColors.STRING);
    public static final TextAttributesKey TYPE_ARGUMENT_ = createTextAttributesKey("OCAML_TYPE_ARGUMENT", TYPE_ARGUMENT_KEY);
    private static final TextAttributesKey BAD_CHAR_ = createTextAttributesKey("OCAML_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER);

    private static final TextAttributesKey[] NUMBER_KEYS = new TextAttributesKey[]{NUMBER_};
    private static final TextAttributesKey[] COMMENT_KEYS = new TextAttributesKey[]{RML_COMMENT_};
    private static final TextAttributesKey[] STRING_KEYS = new TextAttributesKey[]{STRING_};
    private static final TextAttributesKey[] TYPE_ARGUMENT_KEYS = new TextAttributesKey[]{TYPE_ARGUMENT_};
    private static final TextAttributesKey[] POLY_VARIANT_KEYS = new TextAttributesKey[]{POLY_VARIANT_};
    private static final TextAttributesKey[] BRACKET_KEYS = new TextAttributesKey[]{BRACKETS_};
    private static final TextAttributesKey[] BRACE_KEYS = new TextAttributesKey[]{BRACES_};
    private static final TextAttributesKey[] PAREN_KEYS = new TextAttributesKey[]{PARENS_};
    private static final TextAttributesKey[] OPTION_KEYS = new TextAttributesKey[]{OPTION_};
    private static final TextAttributesKey[] KEYWORD_KEYS = new TextAttributesKey[]{KEYWORD_};
    private static final TextAttributesKey[] SEMICOLON_KEYS = new TextAttributesKey[]{SEMICOLON_};
    private static final TextAttributesKey[] DOT_KEYS = new TextAttributesKey[]{OPERATION_SIGN_};
    private static final TextAttributesKey[] COMMA_KEYS = new TextAttributesKey[]{OPERATION_SIGN_};
    private static final TextAttributesKey[] OPERATION_SIGN_KEYS = new TextAttributesKey[]{OPERATION_SIGN_};
    private static final TextAttributesKey[] BAD_CHAR_KEYS = new TextAttributesKey[]{BAD_CHAR_};
    private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];

    @Override public @NotNull Lexer getHighlightingLexer() {
        return new OclLexerAdapter();
    }

    @Override public TextAttributesKey @NotNull [] getTokenHighlights(IElementType tokenType) {
        if (tokenType.equals(OclTypes.MULTI_COMMENT) || tokenType.equals(OclTypes.SINGLE_COMMENT)) {
            return COMMENT_KEYS;
        } else if (tokenType.equals(OclTypes.LBRACE) || tokenType.equals(OclTypes.RBRACE)) {
            return BRACE_KEYS;
        } else if (tokenType.equals(OclTypes.LBRACKET)
                || tokenType.equals(OclTypes.RBRACKET)
                || tokenType.equals(OclTypes.LARRAY)
                || tokenType.equals(OclTypes.RARRAY)) {
            return BRACKET_KEYS;
        } else if (tokenType.equals(OclTypes.LPAREN) || tokenType.equals(OclTypes.RPAREN)) {
            return PAREN_KEYS;
        } else if (tokenType.equals(OclTypes.INT_VALUE) || tokenType.equals(OclTypes.FLOAT_VALUE)) {
            return NUMBER_KEYS;
        } else if (OclTypes.DOT.equals(tokenType)) {
            return DOT_KEYS;
        } else if (OclTypes.TYPE_ARGUMENT.equals(tokenType)) {
            return TYPE_ARGUMENT_KEYS;
        } else if (OclTypes.POLY_VARIANT.equals(tokenType)) {
            return POLY_VARIANT_KEYS;
        } else if (OclTypes.COMMA.equals(tokenType)) {
            return COMMA_KEYS;
        } else if (OclTypes.SEMI.equals(tokenType) || OclTypes.SEMISEMI.equals(tokenType)) {
            return SEMICOLON_KEYS;
        } else if (OclTypes.STRING_VALUE.equals(tokenType) || OclTypes.CHAR_VALUE.equals(tokenType)) {
            return STRING_KEYS;
        } else if (OCL_KEYWORD_TYPES.contains(tokenType)) {
            return KEYWORD_KEYS;
        } else if (OCL_OPERATION_SIGN_TYPES.contains(tokenType)) {
            return OPERATION_SIGN_KEYS;
        } else if (OCL_OPTIONS_TYPES.contains(tokenType)) {
            return OPTION_KEYS;
        } else if (BAD_CHARACTER.equals(tokenType)) {
            return BAD_CHAR_KEYS;
        }

        return EMPTY_KEYS;
    }

    @NotNull
    private static Set<IElementType> of(IElementType... types) {
        Set<IElementType> result = new HashSet<>();
        Collections.addAll(result, types);
        return result;
    }

    private static final Set<IElementType> OCL_KEYWORD_TYPES =
            of(
                    OclTypes.OPEN,
                    OclTypes.MODULE,
                    OclTypes.FUN,
                    OclTypes.LET,
                    OclTypes.TYPE,
                    OclTypes.INCLUDE,
                    OclTypes.EXTERNAL,
                    OclTypes.IF,
                    OclTypes.ELSE,
                    OclTypes.ENDIF,
                    OclTypes.SWITCH,
                    OclTypes.TRY,
                    OclTypes.RAISE,
                    OclTypes.FOR,
                    OclTypes.IN,
                    OclTypes.TO,
                    OclTypes.BOOL_VALUE,
                    OclTypes.REF,
                    OclTypes.EXCEPTION,
                    OclTypes.WHEN,
                    OclTypes.AND,
                    OclTypes.REC,
                    OclTypes.WHILE,
                    OclTypes.ASR,
                    OclTypes.CLASS,
                    OclTypes.CONSTRAINT,
                    OclTypes.DOWNTO,
                    OclTypes.FUNCTOR,
                    OclTypes.INHERIT,
                    OclTypes.INITIALIZER,
                    OclTypes.LAND,
                    OclTypes.LOR,
                    OclTypes.LSL,
                    OclTypes.LSR,
                    OclTypes.LXOR,
                    OclTypes.METHOD,
                    OclTypes.MOD,
                    OclTypes.NEW,
                    OclTypes.NONREC,
                    OclTypes.OR,
                    OclTypes.PRIVATE,
                    OclTypes.VIRTUAL,
                    OclTypes.AS,
                    OclTypes.MUTABLE,
                    OclTypes.OF,
                    OclTypes.VAL,
                    OclTypes.PRI,
                    // OCaml
                    OclTypes.MATCH,
                    OclTypes.WITH,
                    OclTypes.DO,
                    OclTypes.DONE,
                    //OclTypes.RECORD,
                    OclTypes.BEGIN,
                    OclTypes.END,
                    OclTypes.LAZY,
                    OclTypes.ASSERT,
                    OclTypes.THEN,
                    OclTypes.FUNCTION,
                    OclTypes.STRUCT,
                    OclTypes.SIG,
                    OclTypes.OBJECT,
                    OclTypes.DIRECTIVE_IF,
                    OclTypes.DIRECTIVE_ELSE,
                    OclTypes.DIRECTIVE_ELIF,
                    OclTypes.DIRECTIVE_END,
                    OclTypes.DIRECTIVE_ENDIF);

    private static final Set<IElementType> OCL_OPERATION_SIGN_TYPES = of(
            OclTypes.L_AND,
            OclTypes.L_OR,
            OclTypes.SHORTCUT,
            OclTypes.ARROW,
            OclTypes.PIPE_FORWARD,
            OclTypes.EQEQEQ,
            OclTypes.EQEQ,
            OclTypes.EQ,
            OclTypes.NOT_EQEQ,
            OclTypes.NOT_EQ,
            OclTypes.OP_STRUCT_DIFF,
            OclTypes.COLON,
            OclTypes.SINGLE_QUOTE,
            //OclTypes.DOUBLE_QUOTE,
            OclTypes.CARRET,
            OclTypes.PLUSDOT,
            OclTypes.MINUSDOT,
            OclTypes.SLASHDOT,
            OclTypes.STARDOT,
            OclTypes.PLUS,
            OclTypes.MINUS,
            OclTypes.SLASH,
            OclTypes.STAR,
            OclTypes.PERCENT,
            OclTypes.PIPE,
            OclTypes.ARROBASE,
            OclTypes.SHARP,
            OclTypes.SHARPSHARP,
            OclTypes.QUESTION_MARK,
            OclTypes.EXCLAMATION_MARK,
            OclTypes.LT_OR_EQUAL,
            OclTypes.GT_OR_EQUAL,
            OclTypes.AMPERSAND,
            OclTypes.LEFT_ARROW,
            OclTypes.RIGHT_ARROW,
            OclTypes.COLON_EQ,
            OclTypes.COLON_GT,
            OclTypes.GT,
            //OclTypes.GT_BRACE,
            //OclTypes.GT_BRACKET,
            //OclTypes.BRACKET_GT,
            //OclTypes.BRACKET_LT,
            //OclTypes.BRACE_LT,
            OclTypes.DOTDOT
    );

    private static final Set<IElementType> OCL_OPTIONS_TYPES =
            of(OclTypes.NONE, OclTypes.SOME);
}
