package com.ocaml.language.psi

import com.intellij.lang.ASTNode
import com.ocaml.language.psi.api.OCamlStubElementType
import com.ocaml.language.psi.stubs.OCamlLetBindingStub

// nothing for now
class OCamlImplUtils

fun createStubIfParentIsStub(node: ASTNode): Boolean {
    return true
//    val parent = node.treeParent
//    val parentType = parent.elementType
//    return (parentType is IStubElementType<*, *> && parentType.shouldCreateStub(parent)) ||
//            parentType is IStubFileElementType<*>
}

fun factory(name: String): OCamlStubElementType<*, *> = when (name) {
    "LET_BINDING" -> OCamlLetBindingStub.Type
    else -> error("Unknown element $name")
}