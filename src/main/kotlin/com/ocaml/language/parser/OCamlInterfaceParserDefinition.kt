package com.ocaml.language.parser

import com.intellij.lang.PsiParser
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IFileElementType
import com.ocaml.ide.files.OCamlInterfaceLanguage
import com.ocaml.language.base.BaseParserDefinition
import com.ocaml.language.psi.files.OCamlInterfaceFile

class OCamlInterfaceParserDefinition : BaseParserDefinition() {
    override fun createParser(project: Project?): PsiParser = OCamlParser()
    override fun getFileNodeType(): IFileElementType = ParserDefinitionUtils.FILE
    override fun createFile(viewProvider: FileViewProvider): PsiFile = OCamlInterfaceFile(viewProvider)

    object ParserDefinitionUtils {
        val FILE = IFileElementType(OCamlInterfaceLanguage)
    }
}