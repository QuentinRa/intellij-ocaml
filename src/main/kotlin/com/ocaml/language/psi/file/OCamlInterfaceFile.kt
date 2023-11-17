package com.ocaml.language.psi.file

import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import com.ocaml.ide.files.OCamlInterfaceFileType

class OCamlInterfaceFile(viewProvider: FileViewProvider) : OCamlFileBase(viewProvider) {
    override fun getFileType(): FileType = OCamlInterfaceFileType
    override fun toString(): String = "OCaml Interface File"
    override fun isInterface(): Boolean = true
}