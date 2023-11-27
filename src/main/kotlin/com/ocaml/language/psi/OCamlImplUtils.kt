package com.ocaml.language.psi

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType
import com.ocaml.language.psi.api.OCamlStubElementType
import com.ocaml.language.psi.stubs.OCamlLetBindingStub
import com.ocaml.language.psi.stubs.OCamlValBindingStub

// nothing for now
class OCamlImplUtils {
    companion object {
        @JvmStatic
        fun nextSiblingWithTokenType(root: PsiElement, elementType: IElementType): PsiElement? {
            var found: PsiElement? = null
            var sibling = root.nextSibling
            while (sibling != null) {
                if (sibling.node.elementType === elementType) {
                    found = sibling
                    sibling = null
                } else {
                    sibling = sibling.nextSibling
                }
            }
            return found
        }
    }
}

fun createStubIfParentIsStub(node: ASTNode): Boolean {
    return true
//    val parent = node.treeParent
//    val parentType = parent.elementType
//    return (parentType is IStubElementType<*, *> && parentType.shouldCreateStub(parent)) ||
//            parentType is IStubFileElementType<*>
}

fun factory(name: String): OCamlStubElementType<*, *> = when (name) {
    "LET_BINDING" -> OCamlLetBindingStub.Type
    "VALUE_DESCRIPTION" -> OCamlValBindingStub.Type
    else -> error("Unknown element $name")
}