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
            OPEN,
            MODULE,
            FUN,
            LET,
            TYPE,
            INCLUDE,
            EXTERNAL,
            IF,
            ELSE,
            TRY,
            FOR,
            IN,
            TO,
            TRUE,
            FALSE,
            EXCEPTION,
            WHEN,
            AND,
            REC,
            WHILE,
            ASR,
            CLASS,
            CONSTRAINT,
            DOWNTO,
            FUNCTOR,
            INHERIT,
            INITIALIZER,
            LAND,
            LOR,
            LSL,
            LSR,
            LXOR,
            METHOD,
            MOD,
            NEW,
            NONREC,
            OR,
            PRIVATE,
            VIRTUAL,
            AS,
            MUTABLE,
            OF,
            VAL,
            MATCH,
            WITH,
            DO,
            DONE,
            BEGIN,
            END,
            LAZY,
            ASSERT,
            THEN,
            FUNCTION,
            STRUCT,
            SIG,
            OBJECT,
        )
        private val OCAML_OPERATORS = setOf<IElementType>(
            L_AND,
            L_OR,
            SHORTCUT,
            EQ,
            NOT_EQ,
            COLON,
            SINGLE_QUOTE,
            CARRET,
            MINUSDOT,
            PLUS,
            MINUS,
            SLASH,
            STAR,
            PERCENT,
            PIPE,
            SHARP,
            QUESTION_MARK,
            EXCLAMATION_MARK,
            AMPERSAND,
            LEFT_ARROW,
            RIGHT_ARROW,
            COLON_EQ,
            COLON_GT,
            GT,
            DOTDOT
        )
    }
}