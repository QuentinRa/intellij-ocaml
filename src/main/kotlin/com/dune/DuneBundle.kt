package com.dune

import com.intellij.DynamicBundle
import org.jetbrains.annotations.PropertyKey

private const val BUNDLE = "messages.DuneBundle"

// ex: DuneBundle.message("filetype.dune.description")
object DuneBundle : DynamicBundle(BUNDLE) {
    fun message(@PropertyKey(resourceBundle = BUNDLE) key: String,
                vararg params: Any): String {
        return getMessage(key, *params)
    }
}