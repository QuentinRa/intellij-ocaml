package com.dune.language.parser

import com.dune.language.DuneLanguage
import com.dune.language.lexer.DuneLexerAdapter
import com.dune.language.psi.DuneTypes
import com.dune.language.psi.files.DuneFile
import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet

class DuneParserDefinition : ParserDefinition {
    override fun createLexer(project: Project?): Lexer = DuneLexerAdapter()

    override fun createParser(project: Project?): PsiParser = DuneParser()

    override fun getFileNodeType(): IFileElementType = FILE

    override fun getCommentTokens(): TokenSet = COMMENT_TOKENS

    override fun getStringLiteralElements(): TokenSet = STRING_TOKENS

    override fun createElement(node: ASTNode): PsiElement = DuneTypes.Factory.createElement(node.elementType)

    override fun createFile(viewProvider: FileViewProvider): PsiFile = DuneFile(viewProvider)

    companion object Utils {
        val FILE = IFileElementType(DuneLanguage)
        val COMMENT_TOKENS = TokenSet.create(DuneTypes.COMMENT)
        val STRING_TOKENS = TokenSet.create(DuneTypes.STRING_VALUE)
    }
}