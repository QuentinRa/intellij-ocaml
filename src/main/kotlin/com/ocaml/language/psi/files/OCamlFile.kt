package com.ocaml.language.psi.files

import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import com.ocaml.ide.files.OCamlFileType
import com.ocaml.ide.files.OCamlLanguage
import com.ocaml.language.base.OCamlFileBase

class OCamlFile(viewProvider: FileViewProvider) : OCamlFileBase(viewProvider, OCamlLanguage) {
    override fun getFileType(): FileType = OCamlFileType
    override fun toString(): String = "OCaml File"
    override fun isInterface(): Boolean = false
}