package com.dune.language

import com.intellij.lang.Language

/**
 * Dune Language
 */
object DuneLanguage : Language("Dune") {
    private fun readResolve(): Any = DuneLanguage
    override fun getDisplayName(): String = "Dune"
    override fun isCaseSensitive(): Boolean = true
}