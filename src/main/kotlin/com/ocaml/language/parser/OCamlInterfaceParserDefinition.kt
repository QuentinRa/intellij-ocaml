package com.ocaml.language.parser

import com.intellij.lang.PsiParser
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IFileElementType
import com.ocaml.language.base.OCamlBaseParserDefinition
import com.ocaml.language.psi.files.OCamlInterfaceFile
import com.ocaml.language.psi.files.OCamlInterfaceFileStub

class OCamlInterfaceParserDefinition : OCamlBaseParserDefinition() {
    override fun createParser(project: Project?): PsiParser = OCamlInterfaceParser()
    override fun getFileNodeType(): IFileElementType = OCamlInterfaceFileStub.Type
    override fun createFile(viewProvider: FileViewProvider): PsiFile = OCamlInterfaceFile(viewProvider)
}