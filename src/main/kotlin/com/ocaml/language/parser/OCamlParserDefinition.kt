package com.ocaml.language.parser

import com.intellij.lang.PsiParser
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IFileElementType
import com.ocaml.language.base.OCamlBaseParserDefinition
import com.ocaml.language.psi.files.OCamlFile
import com.ocaml.language.psi.files.OCamlFileStub

class OCamlParserDefinition : OCamlBaseParserDefinition() {
    override fun createParser(project: Project?): PsiParser = OCamlParser()
    override fun getFileNodeType(): IFileElementType = OCamlFileStub.Type
    override fun createFile(viewProvider: FileViewProvider): PsiFile = OCamlFile(viewProvider)
}