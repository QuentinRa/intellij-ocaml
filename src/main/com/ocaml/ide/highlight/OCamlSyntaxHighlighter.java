package com.ocaml.ide.highlight;

import com.intellij.lexer.*;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.editor.colors.*;
import com.intellij.openapi.fileTypes.*;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.*;
import com.or.lang.OCamlTypes;
import com.ocaml.lang.lexer.OCamlLexerAdapter;
import org.jetbrains.annotations.*;

import java.util.*;

public class OCamlSyntaxHighlighter implements SyntaxHighlighter {
    /** create Text attributes **/
    private static @NotNull TextAttributesKey createTA(String name, TextAttributesKey fallbackAttributeKey) {
        return TextAttributesKey.createTextAttributesKey("OCAML_"+name, fallbackAttributeKey);
    }

    private static final TextAttributesKey TYPE_ARGUMENT_KEY = TextAttributesKey.createTextAttributesKey("TYPE_ARGUMENT");
    public static final TextAttributesKey ANNOTATION_ = createTA("ANNOTATION", DefaultLanguageHighlighterColors.METADATA);
    public static final TextAttributesKey BRACES_ = createTA("BRACES", DefaultLanguageHighlighterColors.BRACES);
    public static final TextAttributesKey BRACKETS_ = createTA("BRACKETS", DefaultLanguageHighlighterColors.BRACKETS);
    public static final TextAttributesKey DOC_COMMENT = createTA("DOC", DefaultLanguageHighlighterColors.DOC_COMMENT);
    public static final TextAttributesKey KEYWORD_ = createTA("KEYWORD", DefaultLanguageHighlighterColors.KEYWORD);
    public static final TextAttributesKey NUMBER_ = createTA("NUMBER", DefaultLanguageHighlighterColors.NUMBER);
    public static final TextAttributesKey OPERATION_SIGN_ = createTA("OPERATION_SIGN", DefaultLanguageHighlighterColors.OPERATION_SIGN);
    public static final TextAttributesKey OPTION_ = createTA("OPTION", DefaultLanguageHighlighterColors.STATIC_FIELD);
    public static final TextAttributesKey PARENS_ = createTA("PARENS", DefaultLanguageHighlighterColors.PARENTHESES);
    public static final TextAttributesKey POLY_VARIANT_ = createTA("POLY_VARIANT", DefaultLanguageHighlighterColors.STATIC_FIELD);
    public static final TextAttributesKey COMMENT_ = createTA("COMMENT", DefaultLanguageHighlighterColors.BLOCK_COMMENT);
    public static final TextAttributesKey SEMICOLON_ = createTA("SEMICOLON", DefaultLanguageHighlighterColors.SEMICOLON);
    public static final TextAttributesKey STRING_ = createTA("STRING", DefaultLanguageHighlighterColors.STRING);
    public static final TextAttributesKey TYPE_ARGUMENT_ = createTA("TYPE_ARGUMENT", TYPE_ARGUMENT_KEY);
    private static final TextAttributesKey BAD_CHAR_ = createTA("BAD_CHARACTER", HighlighterColors.BAD_CHARACTER);

    // OR
    public static final TextAttributesKey CODE_LENS_ = createTA("CODE_LENS", DefaultLanguageHighlighterColors.BLOCK_COMMENT);
    public static final TextAttributesKey INTERPOLATED_REF_ = createTA("INTERPOLATED_REF", DefaultLanguageHighlighterColors.IDENTIFIER);
    public static final TextAttributesKey MARKUP_TAG_ = createTA("MARKUP_TAG", DefaultLanguageHighlighterColors.MARKUP_TAG);
    public static final TextAttributesKey MARKUP_ATTRIBUTE_ = createTA("MARKUP_ATTRIBUTE", DefaultLanguageHighlighterColors.MARKUP_ATTRIBUTE);
    public static final TextAttributesKey MODULE_NAME_ = createTA("MODULE_NAME", DefaultLanguageHighlighterColors.CLASS_NAME);
    public static final TextAttributesKey VARIANT_NAME_ = createTA("VARIANT_NAME", DefaultLanguageHighlighterColors.STATIC_FIELD);

    private static final TextAttributesKey[] ANNOTATION_KEY = new TextAttributesKey[]{ANNOTATION_};
    private static final TextAttributesKey[] NUMBER_KEYS = new TextAttributesKey[]{NUMBER_};
    private static final TextAttributesKey[] COMMENT_KEYS = new TextAttributesKey[]{COMMENT_};
    private static final TextAttributesKey[] DOC_COMMENT_KEY = new TextAttributesKey[]{DOC_COMMENT};
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
        }  else if (OCamlTypes.ANNOTATION.equals(tokenType)) {
            return ANNOTATION_KEY;
        } else if (OCamlTypes.DOC_COMMENT.equals(tokenType))
            return DOC_COMMENT_KEY;
        else if (TokenType.BAD_CHARACTER.equals(tokenType)) {
            return BAD_CHAR_KEYS;
        }

        return EMPTY_KEYS;
    }

    private static final Set<IElementType> OCL_KEYWORD_TYPES =
            Set.of(
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

    private static final Set<IElementType> OCL_OPERATION_SIGN_TYPES = Set.of(
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
            OCamlTypes.CARET,
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
            OCamlTypes.AT_SIGN,
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
            OCamlTypes.DOTDOT
    );

    private static final Set<IElementType> OCL_OPTIONS_TYPES = Set.of(OCamlTypes.NONE, OCamlTypes.SOME);
}
