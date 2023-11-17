package com.ocaml.language.psi.file

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.psi.FileViewProvider
import com.ocaml.ide.files.OCamlLanguage

abstract class OCamlFileBase(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, OCamlLanguage) {
    abstract fun isInterface() : Boolean
}