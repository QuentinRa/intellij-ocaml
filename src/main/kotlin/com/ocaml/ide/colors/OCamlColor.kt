package com.ocaml.ide.colors

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors as Default

/**
 * All tokens that can have a color
 */
enum class OCamlColor(name: String, attr: TextAttributesKey? = null) {
    VARIABLE("VARIABLE", Default.IDENTIFIER),
    FIELD("FIELD", Default.INSTANCE_FIELD),
    CONSTANT("CONSTANT", Default.CONSTANT),

    FUNCTION("FUNCTION", Default.FUNCTION_DECLARATION),
    METHOD("METHOD", Default.INSTANCE_METHOD),
    FUNCTION_CALL("FUNCTION_CALL", Default.FUNCTION_CALL),
    DIRECTIVE("DIRECTIVE", Default.IDENTIFIER),

    PARAMETER("PARAMETER", Default.PARAMETER),
    TYPE_PARAMETER("TYPE_PARAMETER", Default.IDENTIFIER),
    CONST_PARAMETER("CONST_PARAMETER", Default.CONSTANT),

    TYPE("TYPE", Default.KEYWORD),
    CLASS("CLASS", Default.CLASS_NAME),
    MODULE("MODULE", Default.IDENTIFIER),
    ALIAS("ALIAS", Default.CLASS_NAME),

    KEYWORD("KEYWORD", Default.KEYWORD),

    CHAR("CHAR", Default.STRING),
    NUMBER("NUMBER", Default.NUMBER),
    STRING("STRING", Default.STRING),

    COMMENT("COMMENT", Default.BLOCK_COMMENT),

    DOC_COMMENT("DOC_COMMENT", Default.DOC_COMMENT),

    BRACES("BRACES", Default.BRACES),
    BRACKETS("BRACKETS", Default.BRACKETS),
    OPERATORS("OPERATORS", Default.OPERATION_SIGN),
    SEMICOLON("SEMICOLON", Default.SEMICOLON),
    DOT("DOT", Default.DOT),
    COMMA("COMMA", Default.COMMA),
    PARENTHESES("PARENTHESES", Default.PARENTHESES),

    ATTRIBUTE("ATTRIBUTE", Default.METADATA),
    ;

    val textAttributesKey = TextAttributesKey.createTextAttributesKey("com.ocaml.$name", attr)
}