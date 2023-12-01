package com.ocaml.language.psi.stubs.index

import org.junit.Test

class OCamlLetIndexTest : BaseIndexTest() {
    @Test
    fun testSimpleStatements() {
        // Test duplicate, nested, and anonymous
        val indexSink = testIndex("A.ml", """
                let a = ()
                let a = ()
                let c = 
                    let d = 5
                    in d * d
                let _ = ()
            """)
        assertEquals(3, indexSink.total)
        assertEquals(2, indexSink.namedIndexValuesCount["A.a"])
        assertEquals(1, indexSink.namedIndexValuesCount["A.c"])
    }

    @Test
    fun testPatternStatements() {
        // every pattern statement is a stub
        // e.g., they are tested in the stubs category
        // We only need to test one pattern here
        val indexSink = testIndex("A.ml", """
                let a,b = ()
            """)
        assertEquals(2, indexSink.total)
        assertEquals(1, indexSink.namedIndexValuesCount["A.a"])
        assertEquals(1, indexSink.namedIndexValuesCount["A.b"])
    }
}