package com.ocaml.language.parser

import com.intellij.lang.PsiParser
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiFile
import com.ocaml.language.psi.file.OCamlInterfaceFile

class OCamlParserInterfaceDefinition : OCamlBaseParserDefinition() {
    override fun createParser(project: Project?): PsiParser = OCamlImplementationParser()
    override fun createFile(viewProvider: FileViewProvider): PsiFile = OCamlInterfaceFile(viewProvider)
}