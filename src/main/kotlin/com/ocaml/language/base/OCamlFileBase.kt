package com.ocaml.language.base

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.lang.Language
import com.intellij.psi.FileViewProvider
import com.ocaml.language.OCamlLanguageUtils
import com.ocaml.language.psi.api.OCamlQualifiedNamedElement

abstract class OCamlFileBase(viewProvider: FileViewProvider, language: Language) : PsiFileBase(viewProvider, language),
    OCamlQualifiedNamedElement {
    abstract fun isInterface() : Boolean
    override fun getQualifiedName(): String? = OCamlLanguageUtils.fileNameToModuleName(name)
}