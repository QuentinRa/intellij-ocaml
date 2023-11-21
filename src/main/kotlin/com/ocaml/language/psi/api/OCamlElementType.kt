package com.ocaml.language.psi.api

import com.intellij.lang.ASTNode
import com.intellij.psi.tree.ICompositeElementType
import com.intellij.psi.tree.IElementType
import com.ocaml.ide.files.OCamlLanguage
import com.ocaml.language.psi.OCamlTypes

// type of an element
class OCamlElementType(debugName: String) : IElementType(debugName, OCamlLanguage), ICompositeElementType {
    override fun createCompositeNode(): ASTNode {
        return OCamlTypes.Factory.createElement(this)
    }
}