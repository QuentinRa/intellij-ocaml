package com.ocaml.language.psi

import com.ocaml.language.OCamlParsingTestCase
import org.junit.Test

class JokeOnYou : OCamlParsingTestCase() {
    @Test
    fun test_parser() {
        parseCode("aaa")
    }
}