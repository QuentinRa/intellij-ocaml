package com.intellij.codeInsight.template

open class TemplateContextTypePatch(presentableName: String) : TemplateContextType(computeID(presentableName), presentableName) {
    // Before 2023, ID was not in the XML
    // It was specified in the code
    companion object {
        fun computeID(presentableName: String): String {
            return when (presentableName) {
                "OCaml" -> "OCAML_FILE"
                else -> error("Cannot simulate backward compatibility for: $presentableName")
            }
        }
    }

}