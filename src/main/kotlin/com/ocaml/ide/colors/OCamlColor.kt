package com.ocaml.ide.colors

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.options.OptionsBundle
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.ocaml.OCamlBundle
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors as Default

/**
 * All tokens that can have a color
 */
enum class OCamlColor(humanText: String, attr: TextAttributesKey? = null) {
    VARIABLE(OCamlBundle.message("settings.ocaml.color.variables.default"), Default.IDENTIFIER),
    FIELD(OCamlBundle.message("settings.ocaml.color.field"), Default.INSTANCE_FIELD),
    CONSTANT(OCamlBundle.message("settings.ocaml.color.constant"), Default.CONSTANT),

    FUNCTION(OCamlBundle.message("settings.ocaml.color.function.declaration"), Default.FUNCTION_DECLARATION),
    METHOD(OCamlBundle.message("settings.ocaml.color.method.declaration"), Default.INSTANCE_METHOD),
    FUNCTION_CALL(OCamlBundle.message("settings.ocaml.color.function.call"), Default.FUNCTION_CALL),
    DIRECTIVE(OCamlBundle.message("settings.ocaml.color.directives"), Default.IDENTIFIER),

    PARAMETER(OCamlBundle.message("settings.ocaml.color.parameter"), Default.PARAMETER),
    TYPE_PARAMETER(OCamlBundle.message("settings.ocaml.color.type.parameter"), Default.IDENTIFIER),

    TYPE_NAME(OCamlBundle.message("settings.ocaml.color.type"), Default.KEYWORD),
    CLASS_NAME(OCamlBundle.message("settings.ocaml.color.class"), Default.CLASS_NAME),
    MODULE_NAME(OCamlBundle.message("settings.ocaml.color.module"), Default.IDENTIFIER),
    ALIAS(OCamlBundle.message("settings.ocaml.color.type.alias"), Default.CLASS_NAME),

    KEYWORD(OCamlBundle.message("settings.ocaml.color.keyword"), Default.KEYWORD),

    CHAR(OCamlBundle.message("settings.ocaml.color.char"), Default.STRING),
    NUMBER(OCamlBundle.message("settings.ocaml.color.number"), Default.NUMBER),
    STRING(OCamlBundle.message("settings.ocaml.color.string"), Default.STRING),

    COMMENT(OCamlBundle.message("settings.ocaml.color.comment"), Default.BLOCK_COMMENT),

    DOC_COMMENT(OCamlBundle.message("settings.ocaml.color.doc.comment"), Default.DOC_COMMENT),

    BRACES(OptionsBundle.message("options.language.defaults.braces"), Default.BRACES),
    BRACKETS(OptionsBundle.message("options.language.defaults.brackets"), Default.BRACKETS),
    OPERATORS(OCamlBundle.message("settings.ocaml.color.operation.sign"), Default.OPERATION_SIGN),
    SEMICOLON(OptionsBundle.message("options.language.defaults.semicolon"), Default.SEMICOLON),
    DOT(OptionsBundle.message("options.language.defaults.dot"), Default.DOT),
    COMMA(OptionsBundle.message("options.language.defaults.comma"), Default.COMMA),
    PARENTHESES(OptionsBundle.message("options.language.defaults.parentheses"), Default.PARENTHESES),

    ATTRIBUTE(OCamlBundle.message("settings.ocaml.color.attribute"), Default.METADATA),
    ;

    val textAttributesKey = TextAttributesKey.createTextAttributesKey("com.ocaml.$name", attr)
    val attributesDescriptor = AttributesDescriptor(humanText, textAttributesKey)
}