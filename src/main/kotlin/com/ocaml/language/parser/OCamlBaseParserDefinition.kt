package com.ocaml.language.parser

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import com.ocaml.ide.files.OCamlLanguage
import com.ocaml.language.lexer.OCamlLexerAdapter
import com.ocaml.language.psi.OCamlTypes

abstract class OCamlBaseParserDefinition : ParserDefinition {
    override fun createLexer(project: Project?): Lexer = OCamlLexerAdapter()
    override fun getCommentTokens(): TokenSet = ParserDefinitionUtils.COMMENTS
    override fun getStringLiteralElements(): TokenSet = ParserDefinitionUtils.STRINGS
    override fun getFileNodeType(): IFileElementType = ParserDefinitionUtils.FILE
    override fun createElement(node: ASTNode?): PsiElement = OCamlTypes.Factory.createElement(node)

    object ParserDefinitionUtils {
        val FILE = IFileElementType(OCamlLanguage)
        val COMMENTS = TokenSet.create()
        val STRINGS = TokenSet.create()
//        val COMMENTS = TokenSet.create(OCamlTypes.COMMENT, OCamlTypes.DOC_COMMENT, OCamlTypes.ANNOTATION)
//        val STRINGS = TokenSet.create(OCamlTypes.STRING_LITERAL, OCamlTypes.STRING_VALUE)
    }
}