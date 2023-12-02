package com.dune.ide.highlight

import com.dune.ide.colors.DuneColor
import com.dune.language.lexer.DuneLexerAdapter
import com.dune.language.psi.DuneTypes.*
import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase.pack
import com.intellij.psi.tree.IElementType

class DuneSyntaxHighlighter : SyntaxHighlighter {
    override fun getHighlightingLexer(): Lexer = DuneLexerAdapter()

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> =
        pack(map(tokenType)?.textAttributesKey)

    companion object {
        fun map(tokenType: IElementType): DuneColor? = when (tokenType) {
            COMMENT -> DuneColor.COMMENT
            LPAREN, RPAREN -> DuneColor.PARENTHESES
            STRING_VALUE -> DuneColor.STRING
            else -> null
        }
    }
}