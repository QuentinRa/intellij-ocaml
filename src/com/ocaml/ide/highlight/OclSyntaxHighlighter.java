package com.ocaml.ide.highlight;

import com.intellij.lexer.*;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.editor.colors.*;
import com.intellij.openapi.fileTypes.*;
import com.intellij.psi.tree.*;
import com.reason.lang.ocaml.*;
import org.jetbrains.annotations.*;

import java.util.*;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;
import static com.intellij.psi.TokenType.BAD_CHARACTER;

public class OclSyntaxHighlighter implements SyntaxHighlighter {
    private static final TextAttributesKey TYPE_ARGUMENT_KEY = TextAttributesKey.createTextAttributesKey("TYPE_ARGUMENT");

    public static final TextAttributesKey ANNOTATION_ = createTextAttributesKey("REASONML_ANNOTATION", DefaultLanguageHighlighterColors.METADATA);
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

    private static final TextAttributesKey[] ANNOTATION_KEYS = new TextAttributesKey[]{ANNOTATION_};
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
        return new OclLexer();
    }

    @Override public TextAttributesKey @NotNull [] getTokenHighlights(IElementType tokenType) {
        if (tokenType.equals(OclTypes.INSTANCE.MULTI_COMMENT) || tokenType.equals(OclTypes.INSTANCE.SINGLE_COMMENT)) {
            return COMMENT_KEYS;
        } else if (tokenType.equals(OclTypes.INSTANCE.LBRACE) || tokenType.equals(OclTypes.INSTANCE.RBRACE)) {
            return BRACE_KEYS;
        } else if (tokenType.equals(OclTypes.INSTANCE.LBRACKET)
                || tokenType.equals(OclTypes.INSTANCE.RBRACKET)
                || tokenType.equals(OclTypes.INSTANCE.LARRAY)
                || tokenType.equals(OclTypes.INSTANCE.RARRAY)) {
            return BRACKET_KEYS;
        } else if (tokenType.equals(OclTypes.INSTANCE.LPAREN) || tokenType.equals(OclTypes.INSTANCE.RPAREN)) {
            return PAREN_KEYS;
        } else if (tokenType.equals(OclTypes.INSTANCE.INT_VALUE) || tokenType.equals(OclTypes.INSTANCE.FLOAT_VALUE)) {
            return NUMBER_KEYS;
        } else if (OclTypes.INSTANCE.DOT.equals(tokenType)) {
            return DOT_KEYS;
        } else if (OclTypes.INSTANCE.TYPE_ARGUMENT.equals(tokenType)) {
            return TYPE_ARGUMENT_KEYS;
        } else if (OclTypes.INSTANCE.POLY_VARIANT.equals(tokenType)) {
            return POLY_VARIANT_KEYS;
        } else if (OclTypes.INSTANCE.COMMA.equals(tokenType)) {
            return COMMA_KEYS;
        } else if (OclTypes.INSTANCE.SEMI.equals(tokenType) || OclTypes.INSTANCE.SEMISEMI.equals(tokenType)) {
            return SEMICOLON_KEYS;
        } else if (OclTypes.INSTANCE.STRING_VALUE.equals(tokenType) || OclTypes.INSTANCE.CHAR_VALUE.equals(tokenType)) {
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
        //else if (OclTypes.INSTANCE.ANNOTATION.equals(tokenType)) {
        //    return ANNOTATION_KEYS;
        //}

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
                    OclTypes.INSTANCE.OPEN,
                    OclTypes.INSTANCE.MODULE,
                    OclTypes.INSTANCE.FUN,
                    OclTypes.INSTANCE.LET,
                    OclTypes.INSTANCE.TYPE,
                    OclTypes.INSTANCE.INCLUDE,
                    OclTypes.INSTANCE.EXTERNAL,
                    OclTypes.INSTANCE.IF,
                    OclTypes.INSTANCE.ELSE,
                    OclTypes.INSTANCE.ENDIF,
                    OclTypes.INSTANCE.SWITCH,
                    OclTypes.INSTANCE.TRY,
                    OclTypes.INSTANCE.RAISE,
                    OclTypes.INSTANCE.FOR,
                    OclTypes.INSTANCE.IN,
                    OclTypes.INSTANCE.TO,
                    OclTypes.INSTANCE.BOOL_VALUE,
                    OclTypes.INSTANCE.REF,
                    OclTypes.INSTANCE.EXCEPTION,
                    OclTypes.INSTANCE.WHEN,
                    OclTypes.INSTANCE.AND,
                    OclTypes.INSTANCE.REC,
                    OclTypes.INSTANCE.WHILE,
                    OclTypes.INSTANCE.ASR,
                    OclTypes.INSTANCE.CLASS,
                    OclTypes.INSTANCE.CONSTRAINT,
                    OclTypes.INSTANCE.DOWNTO,
                    OclTypes.INSTANCE.FUNCTOR,
                    OclTypes.INSTANCE.INHERIT,
                    OclTypes.INSTANCE.INITIALIZER,
                    OclTypes.INSTANCE.LAND,
                    OclTypes.INSTANCE.LOR,
                    OclTypes.INSTANCE.LSL,
                    OclTypes.INSTANCE.LSR,
                    OclTypes.INSTANCE.LXOR,
                    OclTypes.INSTANCE.METHOD,
                    OclTypes.INSTANCE.MOD,
                    OclTypes.INSTANCE.NEW,
                    OclTypes.INSTANCE.NONREC,
                    OclTypes.INSTANCE.OR,
                    OclTypes.INSTANCE.PRIVATE,
                    OclTypes.INSTANCE.VIRTUAL,
                    OclTypes.INSTANCE.AS,
                    OclTypes.INSTANCE.MUTABLE,
                    OclTypes.INSTANCE.OF,
                    OclTypes.INSTANCE.VAL,
                    OclTypes.INSTANCE.PRI,
                    // OCaml
                    OclTypes.INSTANCE.MATCH,
                    OclTypes.INSTANCE.WITH,
                    OclTypes.INSTANCE.DO,
                    OclTypes.INSTANCE.DONE,
                    //OclTypes.INSTANCE.RECORD,
                    OclTypes.INSTANCE.BEGIN,
                    OclTypes.INSTANCE.END,
                    OclTypes.INSTANCE.LAZY,
                    OclTypes.INSTANCE.ASSERT,
                    OclTypes.INSTANCE.THEN,
                    OclTypes.INSTANCE.FUNCTION,
                    OclTypes.INSTANCE.STRUCT,
                    OclTypes.INSTANCE.SIG,
                    OclTypes.INSTANCE.OBJECT,
                    OclTypes.INSTANCE.DIRECTIVE_IF,
                    OclTypes.INSTANCE.DIRECTIVE_ELSE,
                    OclTypes.INSTANCE.DIRECTIVE_ELIF,
                    OclTypes.INSTANCE.DIRECTIVE_END,
                    OclTypes.INSTANCE.DIRECTIVE_ENDIF);

    private static final Set<IElementType> OCL_OPERATION_SIGN_TYPES = of(
            OclTypes.INSTANCE.L_AND,
            OclTypes.INSTANCE.L_OR,
            OclTypes.INSTANCE.SHORTCUT,
            OclTypes.INSTANCE.ARROW,
            OclTypes.INSTANCE.PIPE_FORWARD,
            OclTypes.INSTANCE.EQEQEQ,
            OclTypes.INSTANCE.EQEQ,
            OclTypes.INSTANCE.EQ,
            OclTypes.INSTANCE.NOT_EQEQ,
            OclTypes.INSTANCE.NOT_EQ,
            OclTypes.INSTANCE.OP_STRUCT_DIFF,
            OclTypes.INSTANCE.COLON,
            OclTypes.INSTANCE.SINGLE_QUOTE,
            //OclTypes.INSTANCE.DOUBLE_QUOTE,
            OclTypes.INSTANCE.CARRET,
            OclTypes.INSTANCE.PLUSDOT,
            OclTypes.INSTANCE.MINUSDOT,
            OclTypes.INSTANCE.SLASHDOT,
            OclTypes.INSTANCE.STARDOT,
            OclTypes.INSTANCE.PLUS,
            OclTypes.INSTANCE.MINUS,
            OclTypes.INSTANCE.SLASH,
            OclTypes.INSTANCE.STAR,
            OclTypes.INSTANCE.PERCENT,
            OclTypes.INSTANCE.PIPE,
            OclTypes.INSTANCE.ARROBASE,
            OclTypes.INSTANCE.SHARP,
            OclTypes.INSTANCE.SHARPSHARP,
            OclTypes.INSTANCE.QUESTION_MARK,
            OclTypes.INSTANCE.EXCLAMATION_MARK,
            OclTypes.INSTANCE.LT_OR_EQUAL,
            OclTypes.INSTANCE.GT_OR_EQUAL,
            OclTypes.INSTANCE.AMPERSAND,
            OclTypes.INSTANCE.LEFT_ARROW,
            OclTypes.INSTANCE.RIGHT_ARROW,
            OclTypes.INSTANCE.COLON_EQ,
            OclTypes.INSTANCE.COLON_GT,
            OclTypes.INSTANCE.GT,
            //OclTypes.INSTANCE.GT_BRACE,
            //OclTypes.INSTANCE.GT_BRACKET,
            //OclTypes.INSTANCE.BRACKET_GT,
            //OclTypes.INSTANCE.BRACKET_LT,
            //OclTypes.INSTANCE.BRACE_LT,
            OclTypes.INSTANCE.DOTDOT
    );

    private static final Set<IElementType> OCL_OPTIONS_TYPES =
            of(OclTypes.INSTANCE.NONE, OclTypes.INSTANCE.SOME);
}
