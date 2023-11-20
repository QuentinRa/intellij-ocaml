package com.ocaml.language.parser

import com.intellij.lang.PsiParser
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IFileElementType
import com.ocaml.ide.files.OCamlLanguage
import com.ocaml.language.base.BaseParserDefinition
import com.ocaml.language.psi.files.OCamlFile

class OCamlParserDefinition : BaseParserDefinition() {
    override fun createParser(project: Project?): PsiParser = OCamlParser()
    override fun getFileNodeType(): IFileElementType = ParserDefinitionUtils.FILE
    override fun createFile(viewProvider: FileViewProvider): PsiFile = OCamlFile(viewProvider)

    object ParserDefinitionUtils {
        val FILE = IFileElementType(OCamlLanguage)
    }
}