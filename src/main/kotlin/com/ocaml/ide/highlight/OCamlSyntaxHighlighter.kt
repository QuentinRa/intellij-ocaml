package com.ocaml.ide.highlight

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase.pack
import com.intellij.psi.tree.IElementType
import com.ocaml.ide.colors.OCamlColor
import com.ocaml.language.lexer.OCamlLexerAdapter
import com.ocaml.language.psi.OCamlTypes.*

class OCamlSyntaxHighlighter : SyntaxHighlighter {
    override fun getHighlightingLexer(): Lexer = OCamlLexerAdapter()

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> =
        pack(map(tokenType)?.textAttributesKey)

    companion object {
        fun map(tokenType: IElementType): OCamlColor? = when (tokenType) {
            CHAR_VALUE -> OCamlColor.CHAR
            STRING_VALUE -> OCamlColor.STRING
            INTEGER_VALUE -> OCamlColor.NUMBER
            FLOAT_VALUE -> OCamlColor.NUMBER

            COMMENT -> OCamlColor.COMMENT
            DOC_COMMENT -> OCamlColor.DOC_COMMENT
            ANNOTATION -> OCamlColor.ATTRIBUTE

            LPAREN, RPAREN -> OCamlColor.PARENTHESES
            LBRACE, RBRACE -> OCamlColor.BRACES
            LBRACELESS, RGREATERRBRACE -> OCamlColor.BRACES
            LBRACKET, RBRACKET -> OCamlColor.BRACKETS
            LARRAY, RARRAY -> OCamlColor.BRACKETS
            LBRACKETGREATER, LBRACKETLESS -> OCamlColor.BRACKETS

            SEMI, SEMISEMI -> OCamlColor.SEMICOLON
            DOT -> OCamlColor.DOT
            COMMA -> OCamlColor.COMMA

            in OCAML_KEYWORDS -> OCamlColor.KEYWORD
            in OCAML_OPERATORS -> OCamlColor.OPERATORS

            else -> null
        }

        private val OCAML_KEYWORDS = setOf<IElementType>(
            AND, AS, ASR, ASSERT, BEGIN, CLASS, CONSTRAINT,
            DO, DONE, DOWNTO, ELSE, END, EXCEPTION,
            EXTERNAL, FALSE, FOR, FUN, FUNCTION, FUNCTOR,
            IF, IN, INCLUDE, INHERIT, INITIALIZER, LAND, LAZY, LET,
            LOR, LSL, LSR, LXOR, MATCH, METHOD, MOD, MODULE, MUTABLE, NEW,
            NONREC, OBJECT, OF, OPEN, OR, PRIVATE, REC, SIG, STRUCT, THEN, TO,
            TRY, TYPE, VAL, VIRTUAL, WHEN, WHILE, WITH
        )

        private val OCAML_OPERATORS = setOf<IElementType>(
            AMPERSAND, AT_SIGN, BACKTICK, CARET,
            COLON, COLON_EQ, COLON_GT, DOLLAR,
            DOTDOT, EQ, EXCLAMATION_MARK, EXCLAMATION_MARK_MINUS,
            EXCLAMATION_MARK_PLUS, GT, LEFT_ARROW, LT, L_AND, L_OR,
            MINUS, MINUSDOT, NOT_EQ, PERCENT, PIPE, PLUS, PLUSEQ,
            QUESTION_MARK, RIGHT_ARROW, SHARP, SHORTCUT,
            SINGLE_QUOTE, SLASH, STAR, TILDE
        )
    }
}