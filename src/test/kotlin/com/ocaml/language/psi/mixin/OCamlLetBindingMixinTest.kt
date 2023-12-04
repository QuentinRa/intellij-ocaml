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
    private var letOperatorDeconstruction: OCamlLetBinding? = null
    private var letAnonymous: OCamlLetBinding? = null
    private var letAnonymousDeconstruction: OCamlLetBinding? = null

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
            let ((a), (+)) = ()
            let _ = ()
            let (a,(_)) = ()
        """)
        letSimple = letBindings[0]
        letDeconstruction = letBindings[1]
        letDeconstructionComplex = letBindings[2]
        letDeconstructionPipe = letBindings[3]
        letOperatorName = letBindings[4]
        letOperatorNameNoSpace = letBindings[5]
        letOperatorDeconstruction = letBindings[6]
        letAnonymous = letBindings[7]
        letAnonymousDeconstruction = letBindings[8]
    }

    override fun tearDown() {
        super.tearDown()
        letSimple = null
        letDeconstruction = null
        letDeconstructionComplex = null
        letDeconstructionPipe = null
        letOperatorName = null
        letOperatorNameNoSpace = null
        letOperatorDeconstruction = null
        letAnonymous = null
        letAnonymousDeconstruction = null
    }

    @Test
    fun testNameIdentifierIsLeaf() {
        assertInstanceOf(letSimple?.nameIdentifier?.toLeaf(), LeafPsiElement::class.java)
        assertInstanceOf(letOperatorName?.nameIdentifier?.toLeaf(), LeafPsiElement::class.java)
        assertInstanceOf(letOperatorNameNoSpace?.nameIdentifier?.toLeaf(), LeafPsiElement::class.java)

        assertNull(letDeconstruction?.nameIdentifier)
        assertNull(letDeconstructionComplex?.nameIdentifier)
        assertNull(letDeconstructionPipe?.nameIdentifier)
        assertNull(letOperatorDeconstruction?.nameIdentifier)

        assertNull(letAnonymous?.nameIdentifier)
        assertNull(letAnonymousDeconstruction?.nameIdentifier)
    }

    @Test
    fun testName() {
        assertEquals("a", letSimple?.name)
        assertEquals("b,c", letDeconstruction?.name)
        assertEquals("d,e,f", letDeconstructionComplex?.name)
        assertEquals("g,h", letDeconstructionPipe?.name)
        assertEquals("( + )", letOperatorName?.name)
        assertEquals("( + )", letOperatorNameNoSpace?.name)
        assertEquals("a,( + )", letOperatorDeconstruction?.name)

        assertNull(letAnonymous?.name)
        assertEquals("a", letAnonymousDeconstruction?.name)
    }

    @Test
    fun testQualifiedName() {
        // named
        assertEquals(OCAML_FILE_QUALIFIED_NAME_DOT + "a", letSimple?.qualifiedName)
        assertEquals(OCAML_FILE_QUALIFIED_NAME_DOT + "b,c", letDeconstruction?.qualifiedName)
        assertEquals(OCAML_FILE_QUALIFIED_NAME_DOT + "d,e,f", letDeconstructionComplex?.qualifiedName)
        assertEquals(OCAML_FILE_QUALIFIED_NAME_DOT + "g,h", letDeconstructionPipe?.qualifiedName)
        assertEquals("$OCAML_FILE_QUALIFIED_NAME_DOT( + )", letOperatorName?.qualifiedName)
        assertEquals("$OCAML_FILE_QUALIFIED_NAME_DOT( + )", letOperatorNameNoSpace?.qualifiedName)
        assertEquals(OCAML_FILE_QUALIFIED_NAME_DOT+"a,( + )", letOperatorDeconstruction?.qualifiedName)
        // anonymous
        assertNull(letAnonymous?.qualifiedName)
        assertEquals(OCAML_FILE_QUALIFIED_NAME_DOT+"a", letAnonymousDeconstruction?.qualifiedName)
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
        assertExpanded("Dummy.(+)", 1)
        assertExpanded("Dummy.a,b,(+)", 3)
    }

    @Test
    fun testHandleStructuredLetBinding() {
        // Split in PSI elements and returns
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
        assertStructured(letOperatorName, 1)
        assertStructured(letOperatorNameNoSpace, 1)
        assertStructured(letOperatorDeconstruction, 2)
        assertStructured(letAnonymous, 0)
        assertStructured(letAnonymousDeconstruction, 1)
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