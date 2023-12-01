package com.dune.ide.settings

import com.dune.icons.DuneIcons
import com.dune.ide.colors.DuneColor
import com.dune.ide.highlight.DuneSyntaxHighlighter
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import javax.swing.Icon

class DuneColorSettingsPage : ColorSettingsPage {
    override fun getDisplayName(): String = "Dune"
    override fun getIcon(): Icon = DuneIcons.FileTypes.DUNE_FILE
    override fun getAttributeDescriptors() = Constants.ATTRS
    override fun getColorDescriptors(): Array<ColorDescriptor> = ColorDescriptor.EMPTY_ARRAY
    override fun getHighlighter(): SyntaxHighlighter = DuneSyntaxHighlighter()
    override fun getAdditionalHighlightingTagToDescriptorMap() = Constants.ANNOTATOR_TAGS
    override fun getDemoText() = Constants.DEMO_TEXT

    internal object Constants {
        val ATTRS: Array<AttributesDescriptor> = DuneColor.values().map{ it.attributesDescriptor }.toTypedArray()
        val ANNOTATOR_TAGS: Map<String, TextAttributesKey> = DuneColor.values().associateBy({ it.name }, { it.textAttributesKey })
        val DEMO_TEXT: String by lazy {
            """; Simple Dune File
(<INSTRUCTION>library</INSTRUCTION>
 (<ARGUMENT>name</ARGUMENT> <PARAMETER>my_lib</PARAMETER>)
 (<ARGUMENT>modules</ARGUMENT> <PARAMETER>my_module</PARAMETER>))

(<INSTRUCTION>executable</INSTRUCTION>
 (<ARGUMENT>name</ARGUMENT> "my_executable")
 (<ARGUMENT>libraries</ARGUMENT> <PARAMETER>my_lib</PARAMETER>)
 (<ARGUMENT>modules</ARGUMENT> <PARAMETER>main</PARAMETER>))"""
        }
    }

}