package com.ocaml.language

import com.intellij.openapi.util.io.FileUtilRt
import com.ocaml.language.psi.OCamlOperatorName
import java.util.*

// todo: add tests
object OCamlLanguageUtils {
    // convert "file.ml" to "File"
    fun fileNameToModuleName(filename: String): String {
        val nameWithoutExtension = FileUtilRt.getNameWithoutExtension(filename)
        return if (nameWithoutExtension.isEmpty()) "" else nameWithoutExtension.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.getDefault()
            ) else it.toString()
        }
    }

    fun OCamlOperatorName.pretty() : String  = "( $text )"
}