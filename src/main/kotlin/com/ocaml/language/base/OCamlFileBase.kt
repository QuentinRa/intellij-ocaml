package com.ocaml.language.base

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.lang.Language
import com.intellij.psi.FileViewProvider

abstract class OCamlFileBase(viewProvider: FileViewProvider, language: Language) : PsiFileBase(viewProvider, language) {
    abstract fun isInterface() : Boolean
}