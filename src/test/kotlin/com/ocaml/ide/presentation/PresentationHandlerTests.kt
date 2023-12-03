package com.ocaml.ide.presentation

import com.intellij.psi.PsiElement
import com.intellij.ui.icons.allIconsStable
import com.intellij.util.PlatformIcons
import com.ocaml.icons.OCamlIcons
import com.ocaml.ide.OCamlBasePlatformTestCase
import com.ocaml.language.psi.OCamlLetBinding
import com.ocaml.language.psi.OCamlValueDescription
import com.ocaml.language.psi.api.OCamlNamedElement
import org.junit.Test
import javax.swing.Icon

class PresentationHandlerTests : OCamlBasePlatformTestCase() {

    private var simpleVariableAssignation : OCamlLetBinding? = null
    private var simpleFunctionAssignation : OCamlLetBinding? = null
    private var simpleVariableDeclaration : OCamlValueDescription? = null
    private var simpleFunctionDeclaration : OCamlValueDescription? = null

    override fun setUp() {
        super.setUp()
        val ocamlElements = configureOCaml<OCamlNamedElement>(
            """
               let x = 5 
               let f a b = () 
            """
        )
        simpleVariableAssignation = ocamlElements[0] as? OCamlLetBinding
        simpleFunctionAssignation = ocamlElements[1] as? OCamlLetBinding

        val ocamlInterfaceElements = configureOCamlInterface<OCamlNamedElement>(
            """
               val x : int 
               val f : int -> int -> int 
            """
        )
        simpleVariableDeclaration = ocamlInterfaceElements[0] as? OCamlValueDescription
        simpleFunctionDeclaration = ocamlInterfaceElements[1] as? OCamlValueDescription
    }

    override fun tearDown() {
        super.tearDown()
        simpleVariableAssignation = null
        simpleFunctionAssignation = null

        simpleVariableDeclaration = null
        simpleFunctionDeclaration = null
    }

    private fun testPresentationForStructure(e: PsiElement, expectedName: String): List<Icon> {
        val presentation = getPresentationForStructure(e)
        val icon = presentation.getIcon(false)
        assertEquals(expectedName, presentation.presentableText)
        assertNull(presentation.locationString)
        assertNotNull(icon)
        val iconList = icon?.allIconsStable()?.asList()
        assertNotNull(iconList)
        assertSize(2, iconList!!)
        return iconList
    }

    @Test
    fun testNamedPresentationForStructure() {
        // assignations
        val varAssignationIcons = testPresentationForStructure(simpleVariableAssignation!!, "x")
        assertEquals(OCamlIcons.Nodes.VARIABLE, varAssignationIcons[0])
        assertEquals(PlatformIcons.PUBLIC_ICON, varAssignationIcons[1])

        val funAssignationIcons = testPresentationForStructure(simpleFunctionAssignation!!, "f")
        assertEquals(OCamlIcons.Nodes.FUNCTION, funAssignationIcons[0])
        assertEquals(PlatformIcons.PUBLIC_ICON, funAssignationIcons[1])

        // declarations
        val varDeclarationIcons = testPresentationForStructure(simpleVariableDeclaration!!, "x")
        assertEquals(OCamlIcons.Nodes.VARIABLE, varDeclarationIcons[0])
        assertEquals(PlatformIcons.PUBLIC_ICON, varDeclarationIcons[1])

        val funDeclarationIcons = testPresentationForStructure(simpleFunctionDeclaration!!, "f")
        assertEquals(OCamlIcons.Nodes.FUNCTION, funDeclarationIcons[0])
        assertEquals(PlatformIcons.PUBLIC_ICON, funDeclarationIcons[1])
    }
}