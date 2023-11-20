package com.ocaml.language.psi.files

import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import com.ocaml.ide.files.OCamlInterfaceFileType
import com.ocaml.ide.files.OCamlInterfaceLanguage
import com.ocaml.language.base.OCamlFileBase

class OCamlInterfaceFile(viewProvider: FileViewProvider) : OCamlFileBase(viewProvider, OCamlInterfaceLanguage) {
    override fun getFileType(): FileType = OCamlInterfaceFileType
    override fun toString(): String = "OCaml Interface File"
    override fun isInterface(): Boolean = true
}