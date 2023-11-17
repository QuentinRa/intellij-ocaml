package com.ocaml.ide.files

import com.intellij.lang.Language

/**
 * OCaml Language
 */
object OCamlLanguage : Language("OCaml") {
    private fun readResolve(): Any = OCamlLanguage
    override fun getDisplayName(): String = "OCaml"
    override fun isCaseSensitive(): Boolean = true
}