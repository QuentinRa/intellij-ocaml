package com.ocaml.language.psi.mixin

import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.ocaml.language.OCamlParsingTestCase
import com.ocaml.language.psi.OCamlLetBinding
import org.junit.Test

// Add test for let mixin (let f|g = 5)
class OCamlLetBindingMixinTest : OCamlParsingTestCase() {

    // todo: getName ==> expected name
    // ===> x
    // ===> x,y
    // todo: computeValueNames
    // ===> x,y (nodes)
    // todo: expandLetBindingStructuredName
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

}