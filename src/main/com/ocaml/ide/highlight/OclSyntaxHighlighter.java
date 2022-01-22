package com.ocaml.ide.highlight;

import com.intellij.lexer.*;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.editor.colors.*;
import com.intellij.openapi.fileTypes.*;
import com.intellij.psi.tree.*;
import com.ocaml.lang.core.psi.OCamlTypes;
import com.ocaml.lang.lexer.OCamlLexerAdapter;
import org.jetbrains.annotations.*;

import java.util.*;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;
import static com.intellij.psi.TokenType.BAD_CHARACTER;

public class OclSyntaxHighlighter implements SyntaxHighlighter {
    private static final TextAttributesKey TYPE_ARGUMENT_KEY = TextAttributesKey.createTextAttributesKey("TYPE_ARGUMENT");

    public static final TextAttributesKey ANNOTATION_ = createTextAttributesKey("REASONML_ANNOTATION", DefaultLanguageHighlighterColors.METADATA);
    public static final TextAttributesKey BRACES_ = createTextAttributesKey("REASONML_BRACES", DefaultLanguageHighlighterColors.BRACES);
    public static final TextAttributesKey BRACKETS_ = createTextAttributesKey("REASONML_BRACKETS", DefaultLanguageHighlighterColors.BRACKETS);
    public static final TextAttributesKey KEYWORD_ = createTextAttributesKey("REASONML_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD);
    public static final TextAttributesKey INTERPOLATED_REF_ = createTextAttributesKey("REASONML_INTERPOLATED_REF", DefaultLanguageHighlighterColors.IDENTIFIER);
    public static final TextAttributesKey MARKUP_TAG_ = createTextAttributesKey("REASONML_MARKUP_TAG", DefaultLanguageHighlighterColors.MARKUP_TAG);
    public static final TextAttributesKey MARKUP_ATTRIBUTE_ = createTextAttributesKey("REASONML_MARKUP_ATTRIBUTE", DefaultLanguageHighlighterColors.MARKUP_ATTRIBUTE);
    public static final TextAttributesKey MODULE_NAME_ = createTextAttributesKey("REASONML_MODULE_NAME", DefaultLanguageHighlighterColors.CLASS_NAME);
    public static final TextAttributesKey NUMBER_ = createTextAttributesKey("REASONML_NUMBER", DefaultLanguageHighlighterColors.NUMBER);
    public static final TextAttributesKey OPERATION_SIGN_ = createTextAttributesKey("REASONML_OPERATION_SIGN", DefaultLanguageHighlighterColors.OPERATION_SIGN);
    public static final TextAttributesKey OPTION_ = createTextAttributesKey("REASONML_OPTION", DefaultLanguageHighlighterColors.STATIC_FIELD);
    public static final TextAttributesKey PARENS_ = createTextAttributesKey("REASONML_PARENS", DefaultLanguageHighlighterColors.PARENTHESES);
    public static final TextAttributesKey POLY_VARIANT_ = createTextAttributesKey("REASONML_POLY_VARIANT", DefaultLanguageHighlighterColors.STATIC_FIELD);
    public static final TextAttributesKey RML_COMMENT_ = createTextAttributesKey("REASONML_COMMENT", DefaultLanguageHighlighterColors.BLOCK_COMMENT);
    public static final TextAttributesKey SEMICOLON_ = createTextAttributesKey("REASONML_SEMICOLON", DefaultLanguageHighlighterColors.SEMICOLON);
    public static final TextAttributesKey STRING_ = createTextAttributesKey("REASONML_STRING", DefaultLanguageHighlighterColors.STRING);
    public static final TextAttributesKey TYPE_ARGUMENT_ = createTextAttributesKey("REASONML_TYPE_ARGUMENT", TYPE_ARGUMENT_KEY);
    public static final TextAttributesKey VARIANT_NAME_ = createTextAttributesKey("REASONML_VARIANT_NAME", DefaultLanguageHighlighterColors.STATIC_FIELD);
    private static final TextAttributesKey BAD_CHAR_ = createTextAttributesKey("REASONML_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER);

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
        return new OCamlLexerAdapter();
    }

    @Override public TextAttributesKey @NotNull [] getTokenHighlights(IElementType tokenType) {
        if (tokenType.equals(OCamlTypes.MULTI_COMMENT) || tokenType.equals(OCamlTypes.SINGLE_COMMENT)) {
            return COMMENT_KEYS;
        } else if (tokenType.equals(OCamlTypes.LBRACE) || tokenType.equals(OCamlTypes.RBRACE)) {
            return BRACE_KEYS;
        } else if (tokenType.equals(OCamlTypes.LBRACKET)
                || tokenType.equals(OCamlTypes.RBRACKET)
                || tokenType.equals(OCamlTypes.LARRAY)
                || tokenType.equals(OCamlTypes.RARRAY)) {
            return BRACKET_KEYS;
        } else if (tokenType.equals(OCamlTypes.LPAREN) || tokenType.equals(OCamlTypes.RPAREN)) {
            return PAREN_KEYS;
        } else if (tokenType.equals(OCamlTypes.INT_VALUE) || tokenType.equals(OCamlTypes.FLOAT_VALUE)) {
            return NUMBER_KEYS;
        } else if (OCamlTypes.DOT.equals(tokenType)) {
            return DOT_KEYS;
        } else if (OCamlTypes.TYPE_ARGUMENT.equals(tokenType)) {
            return TYPE_ARGUMENT_KEYS;
        } else if (OCamlTypes.POLY_VARIANT.equals(tokenType)) {
            return POLY_VARIANT_KEYS;
        } else if (OCamlTypes.COMMA.equals(tokenType)) {
            return COMMA_KEYS;
        } else if (OCamlTypes.SEMI.equals(tokenType) || OCamlTypes.SEMISEMI.equals(tokenType)) {
            return SEMICOLON_KEYS;
        } else if (OCamlTypes.STRING_VALUE.equals(tokenType) || OCamlTypes.CHAR_VALUE.equals(tokenType)) {
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
                    OCamlTypes.OPEN,
                    OCamlTypes.MODULE,
                    OCamlTypes.FUN,
                    OCamlTypes.LET,
                    OCamlTypes.TYPE,
                    OCamlTypes.INCLUDE,
                    OCamlTypes.EXTERNAL,
                    OCamlTypes.IF,
                    OCamlTypes.ELSE,
                    OCamlTypes.ENDIF,
                    OCamlTypes.SWITCH,
                    OCamlTypes.TRY,
                    OCamlTypes.RAISE,
                    OCamlTypes.FOR,
                    OCamlTypes.IN,
                    OCamlTypes.TO,
                    OCamlTypes.BOOL_VALUE,
                    OCamlTypes.REF,
                    OCamlTypes.EXCEPTION,
                    OCamlTypes.WHEN,
                    OCamlTypes.AND,
                    OCamlTypes.REC,
                    OCamlTypes.WHILE,
                    OCamlTypes.ASR,
                    OCamlTypes.CLASS,
                    OCamlTypes.CONSTRAINT,
                    OCamlTypes.DOWNTO,
                    OCamlTypes.FUNCTOR,
                    OCamlTypes.INHERIT,
                    OCamlTypes.INITIALIZER,
                    OCamlTypes.LAND,
                    OCamlTypes.LOR,
                    OCamlTypes.LSL,
                    OCamlTypes.LSR,
                    OCamlTypes.LXOR,
                    OCamlTypes.METHOD,
                    OCamlTypes.MOD,
                    OCamlTypes.NEW,
                    OCamlTypes.NONREC,
//                    OCamlTypes.NOT,
                    OCamlTypes.OR,
                    OCamlTypes.PRIVATE,
                    OCamlTypes.VIRTUAL,
                    OCamlTypes.AS,
                    OCamlTypes.MUTABLE,
                    OCamlTypes.OF,
                    OCamlTypes.VAL,
                    OCamlTypes.PRI,
                    OCamlTypes.MATCH,
                    OCamlTypes.WITH,
                    OCamlTypes.DO,
                    OCamlTypes.DONE,
                    //OCamlTypes.RECORD,
                    OCamlTypes.BEGIN,
                    OCamlTypes.END,
                    OCamlTypes.LAZY,
                    OCamlTypes.ASSERT,
                    OCamlTypes.THEN,
                    OCamlTypes.FUNCTION,
                    OCamlTypes.STRUCT,
                    OCamlTypes.SIG,
                    OCamlTypes.OBJECT,
                    OCamlTypes.DIRECTIVE_IF,
                    OCamlTypes.DIRECTIVE_ELSE,
                    OCamlTypes.DIRECTIVE_ELIF,
                    OCamlTypes.DIRECTIVE_END,
                    OCamlTypes.DIRECTIVE_ENDIF);

    private static final Set<IElementType> OCL_OPERATION_SIGN_TYPES = of(
            OCamlTypes.L_AND,
            OCamlTypes.L_OR,
            OCamlTypes.SHORTCUT,
            OCamlTypes.ARROW,
            OCamlTypes.PIPE_FORWARD,
            OCamlTypes.EQEQEQ,
            OCamlTypes.EQEQ,
            OCamlTypes.EQ,
            OCamlTypes.NOT_EQEQ,
            OCamlTypes.NOT_EQ,
            OCamlTypes.OP_STRUCT_DIFF,
            OCamlTypes.COLON,
            OCamlTypes.SINGLE_QUOTE,
            //OCamlTypes.DOUBLE_QUOTE,
            OCamlTypes.CARRET,
            OCamlTypes.PLUSDOT,
            OCamlTypes.MINUSDOT,
            OCamlTypes.SLASHDOT,
            OCamlTypes.STARDOT,
            OCamlTypes.PLUS,
            OCamlTypes.MINUS,
            OCamlTypes.SLASH,
            OCamlTypes.STAR,
            OCamlTypes.PERCENT,
            OCamlTypes.PIPE,
            OCamlTypes.ARROBASE,
            OCamlTypes.SHARP,
            OCamlTypes.SHARPSHARP,
            OCamlTypes.QUESTION_MARK,
            OCamlTypes.EXCLAMATION_MARK,
            OCamlTypes.LT_OR_EQUAL,
            OCamlTypes.GT_OR_EQUAL,
            OCamlTypes.AMPERSAND,
            OCamlTypes.LEFT_ARROW,
            OCamlTypes.RIGHT_ARROW,
            OCamlTypes.COLON_EQ,
            OCamlTypes.COLON_GT,
            OCamlTypes.GT,
            //OCamlTypes.GT_BRACE,
            //OCamlTypes.GT_BRACKET,
            //OCamlTypes.BRACKET_GT,
            //OCamlTypes.BRACKET_LT,
            //OCamlTypes.BRACE_LT,
            OCamlTypes.DOTDOT
    );

    private static final Set<IElementType> OCL_OPTIONS_TYPES =
            of(OCamlTypes.NONE, OCamlTypes.SOME);
}
