package com.ocaml.language.psi.mixin

import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.ocaml.language.OCamlParsingTestCase
import com.ocaml.language.psi.OCamlLetBinding
import org.junit.Test

// Add test for let mixin (let f|g = 5)
class OCamlLetBindingMixinTest : OCamlParsingTestCase() {

    // todo: computeValueNames
    // ===> x,y (nodes)
    // todo: handleStructuredLetBinding

    private var letSimple: OCamlLetBinding? = null
    private var letDeconstruction: OCamlLetBinding? = null
    private var letDeconstructionComplex: OCamlLetBinding? = null
    private var letDeconstructionPipe: OCamlLetBinding? = null

    override fun setUp() {
        super.setUp()
        // These expressions are syntactically valid
        // But, they are compiling
        val letBindings = initWith<OCamlLetBinding>("""
            let a = ()
            let b,c = ()
            let ((d,e), f) = ()
            let g|h = ()
        """)
        letSimple = letBindings[0]
        letDeconstruction = letBindings[1]
        letDeconstructionComplex = letBindings[2]
        letDeconstructionPipe = letBindings[3]
    }

    override fun tearDown() {
        super.tearDown()
        letSimple = null
        letDeconstruction = null
        letDeconstructionComplex = null
        letDeconstructionPipe = null
    }

    @Test
    fun testNameIdentifierIsLeaf() {
        assertInstanceOf(letSimple?.nameIdentifier, LeafPsiElement::class.java)
        assertNull(letDeconstruction?.nameIdentifier)
        assertNull(letDeconstructionComplex?.nameIdentifier)
        assertNull(letDeconstructionPipe?.nameIdentifier)
    }

    @Test
    fun testName() {
        assertEquals(letSimple?.name, "a")
        assertEquals(letDeconstruction?.name, "b,c")
        assertEquals(letDeconstructionComplex?.name, "d,e,f")
        assertEquals(letDeconstructionPipe?.name, "g,h")
    }

    @Test
    fun testQualifiedName() {
        assertEquals(letSimple?.qualifiedName, OCAML_FILE_QUALIFIED_NAME_DOT + "a")
        assertEquals(letDeconstruction?.qualifiedName, OCAML_FILE_QUALIFIED_NAME_DOT + "b,c")
        assertEquals(letDeconstructionComplex?.qualifiedName, OCAML_FILE_QUALIFIED_NAME_DOT + "d,e,f")
        assertEquals(letDeconstructionPipe?.qualifiedName, OCAML_FILE_QUALIFIED_NAME_DOT + "g,h")
    }

    @Test
    fun testExpandStructuredName() {
        fun assertExpanded (name: String, count: Int) {
            assertSize(
                count,
                expandLetBindingStructuredName(name)
            )
        }

        assertExpanded("Dummy.a", 1)
        assertExpanded("Dummy.a,b", 2)
        assertExpanded("Dummy.c,d,e", 3)
    }
}