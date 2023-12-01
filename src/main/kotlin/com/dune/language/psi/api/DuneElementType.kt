package com.dune.language.psi.api

import com.dune.language.DuneLanguage
import com.dune.language.psi.DuneTypes
import com.intellij.lang.ASTNode
import com.intellij.psi.tree.ICompositeElementType
import com.intellij.psi.tree.IElementType

// type of one element
class DuneElementType(debugName: String) : IElementType(debugName, DuneLanguage), ICompositeElementType {
    override fun createCompositeNode(): ASTNode {
        return DuneTypes.Factory.createElement(this)
    }
}