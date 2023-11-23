package com.ocaml.language.base

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.lang.Language
import com.intellij.psi.FileViewProvider
import com.ocaml.language.OCamlLanguageUtils
import com.ocaml.language.psi.api.OCamlNamedElement

abstract class OCamlFileBase(viewProvider: FileViewProvider, language: Language) : PsiFileBase(viewProvider, language), OCamlNamedElement {
    abstract fun isInterface() : Boolean
    override fun getName(): String {
        return OCamlLanguageUtils.fileNameToModuleName(super.getName())
    }
}