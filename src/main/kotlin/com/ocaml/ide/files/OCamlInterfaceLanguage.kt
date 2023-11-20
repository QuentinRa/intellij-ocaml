package com.ocaml.ide.files

import com.intellij.lang.Language

/**
 * OCaml Interface File Language
 */
object OCamlInterfaceLanguage : Language(OCamlLanguage, "OCamli") {
    private fun readResolve(): Any = OCamlInterfaceLanguage
    override fun getDisplayName(): String = "OCaml"
    override fun isCaseSensitive(): Boolean = true
}