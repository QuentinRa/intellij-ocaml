package com.ocaml.ide.highlight

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.psi.tree.IElementType
import com.ocaml.language.lexer.OCamlLexerAdapter

class OCamlSyntaxHighlighter : SyntaxHighlighter {
    override fun getHighlightingLexer(): Lexer {
        return OCamlLexerAdapter()
    }

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> {
        return EMPTY_KEYS
    }

    companion object {
        /** create Text attributes  */
        private fun createTA(name: String, fallbackAttributeKey: TextAttributesKey): TextAttributesKey {
            return TextAttributesKey.createTextAttributesKey("OCAML_$name", fallbackAttributeKey)
        }

        private val EMPTY_KEYS = emptyArray<TextAttributesKey>()
    }
}