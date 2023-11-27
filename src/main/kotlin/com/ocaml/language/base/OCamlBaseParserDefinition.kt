package com.ocaml.language.base

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.TokenSet
import com.ocaml.language.lexer.OCamlLexerAdapter
import com.ocaml.language.psi.OCamlTypes

abstract class OCamlBaseParserDefinition : ParserDefinition {
    final override fun createLexer(project: Project?): Lexer = OCamlLexerAdapter()
    final override fun getCommentTokens(): TokenSet = ParserDefinitionUtils.COMMENTS
    final override fun getStringLiteralElements(): TokenSet = ParserDefinitionUtils.STRINGS
    final override fun createElement(node: ASTNode): PsiElement = OCamlTypes.Factory.createElement(node)

    private object ParserDefinitionUtils {
        val COMMENTS = TokenSet.create(OCamlTypes.COMMENT, OCamlTypes.DOC_COMMENT, OCamlTypes.ANNOTATION)
        val STRINGS = TokenSet.create(OCamlTypes.STRING_VALUE)
    }

    companion object {
        const val PARSER_VERSION = 1
    }
}