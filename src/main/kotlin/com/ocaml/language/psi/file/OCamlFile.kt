package com.ocaml.language.psi.file

import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import com.ocaml.ide.files.OCamlFileType

class OCamlFile(viewProvider: FileViewProvider) : OCamlFileBase(viewProvider) {
    override fun getFileType(): FileType = OCamlFileType
    override fun toString(): String = "OCaml File"
    override fun isInterface(): Boolean = false
}