package com.ocaml.language

import com.intellij.openapi.util.io.FileUtilRt
import java.util.*

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
}