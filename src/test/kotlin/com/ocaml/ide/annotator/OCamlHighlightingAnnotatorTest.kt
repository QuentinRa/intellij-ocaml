package com.ocaml.ide.annotator

import com.ocaml.ide.OCamlBasePlatformTestCase
import com.ocaml.ide.colors.OCamlColor
import org.junit.Test

class OCamlHighlightingAnnotatorTest : OCamlBasePlatformTestCase() {

    @Test
    fun testGlobalVariableHighlight() {
        configureHighlight("dummy.ml", "let <info>x</info> = 5", OCamlColor.GLOBAL_VARIABLE)
    }

    @Test
    fun testLocalVariableHighlight() {
        configureHighlight("dummy.ml", "let x = let <info>y</info> = 5 in y * y", OCamlColor.LOCAL_VARIABLE)
    }

    @Test
    fun testFunction() {
        configureHighlight("dummy.ml", "let <info>f</info> a b = ()", OCamlColor.FUNCTION_DECLARATION)
    }
}