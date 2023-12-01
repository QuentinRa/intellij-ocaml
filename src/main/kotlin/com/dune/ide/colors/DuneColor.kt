package com.dune.ide.colors

import com.dune.DuneBundle
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.options.OptionsBundle
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors as Default

/**
 * All tokens that can have a color
 */
enum class DuneColor(humanText: String, attr: TextAttributesKey? = null) {
    COMMENT(OptionsBundle.message("options.language.defaults.line.comment"), Default.BLOCK_COMMENT),
    ATOM(DuneBundle.message("settings.dune.color.atom"), Default.FUNCTION_DECLARATION),
    STRING(OptionsBundle.message("options.language.defaults.string"), Default.STRING),
    PARENTHESES(OptionsBundle.message("options.language.defaults.parentheses"), Default.PARENTHESES),
    ;

    val textAttributesKey = TextAttributesKey.createTextAttributesKey("com.dune.$name", attr)
    val attributesDescriptor = AttributesDescriptor(humanText, textAttributesKey)
}