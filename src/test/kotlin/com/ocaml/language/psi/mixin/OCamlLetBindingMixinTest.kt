package com.ocaml.language.psi.mixin

import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.ocaml.language.OCamlParsingTestCase
import com.ocaml.language.psi.OCamlImplUtils.Companion.toLeaf
import com.ocaml.language.psi.OCamlLetBinding
import org.junit.Test

class OCamlLetBindingMixinTest : OCamlParsingTestCase() {
    private var letSimple: OCamlLetBinding? = null
    private var letDeconstruction: OCamlLetBinding? = null
    private var letDeconstructionComplex: OCamlLetBinding? = null
    private var letDeconstructionPipe: OCamlLetBinding? = null
    private var letOperatorName: OCamlLetBinding? = null
    private var letOperatorNameNoSpace: OCamlLetBinding? = null

    override fun setUp() {
        super.setUp()
        // These expressions are syntactically valid
        // But, they are compiling
        val letBindings = initWith<OCamlLetBinding>("""
            let a = ()
            let b,c = ()
            let ((d,e), f) = ()
            let g|h = ()
            let ( + ) = ()
            let (+) = ()
        """)
        letSimple = letBindings[0]
        letDeconstruction = letBindings[1]
        letDeconstructionComplex = letBindings[2]
        letDeconstructionPipe = letBindings[3]
        letOperatorName = letBindings[4]
        letOperatorNameNoSpace = letBindings[5]
    }

    override fun tearDown() {
        super.tearDown()
        letSimple = null
        letDeconstruction = null
        letDeconstructionComplex = null
        letDeconstructionPipe = null
        letOperatorName = null
        letOperatorNameNoSpace = null
    }

    @Test
    fun testNameIdentifierIsLeaf() {
        assertInstanceOf(letSimple?.nameIdentifier?.toLeaf(), LeafPsiElement::class.java)
        assertInstanceOf(letOperatorName?.nameIdentifier?.toLeaf(), LeafPsiElement::class.java)
        assertInstanceOf(letOperatorNameNoSpace?.nameIdentifier?.toLeaf(), LeafPsiElement::class.java)
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
        assertEquals(letOperatorName?.name, "( + )")
        assertEquals(letOperatorNameNoSpace?.name, "( + )")
    }

    @Test
    fun testQualifiedName() {
        assertEquals(letSimple?.qualifiedName, OCAML_FILE_QUALIFIED_NAME_DOT + "a")
        assertEquals(letDeconstruction?.qualifiedName, OCAML_FILE_QUALIFIED_NAME_DOT + "b,c")
        assertEquals(letDeconstructionComplex?.qualifiedName, OCAML_FILE_QUALIFIED_NAME_DOT + "d,e,f")
        assertEquals(letDeconstructionPipe?.qualifiedName, OCAML_FILE_QUALIFIED_NAME_DOT + "g,h")
        assertEquals(letOperatorName?.qualifiedName, "$OCAML_FILE_QUALIFIED_NAME_DOT( + )")
        assertEquals(letOperatorNameNoSpace?.qualifiedName, "$OCAML_FILE_QUALIFIED_NAME_DOT( + )")
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

    @Test
    fun testHandleStructuredLetBinding() {
        fun assertStructured (letBinding: OCamlLetBinding?, count: Int) {
            assertNotNull(letBinding) ; letBinding!!
            assertSize(
                count,
                handleStructuredLetBinding(letBinding)
            )
        }

        assertStructured(letSimple, 1)
        assertStructured(letDeconstruction, 2)
        assertStructured(letDeconstructionComplex, 3)
        assertStructured(letDeconstructionPipe, 2)
    }

    @Test
    fun testOperatorSpacing() {
        initWith<OCamlLetBinding>("""
            let ( === ) = ()
            let (===) = ()
            let ( ===) = ()
            let ( \n   ===\n) = ()
        """).forEach {
            assertEquals(it.name, "( === )")
        }
    }
}