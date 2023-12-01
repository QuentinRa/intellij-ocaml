package com.dune.language.psi.ext

import com.dune.language.psi.DuneValue
import com.dune.language.psi.api.DuneElementImpl
import com.dune.language.psi.api.DuneNamedOwnerElement
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType
import com.intellij.psi.util.PsiTreeUtil

// Base class for DuneNamedOwnerElement elements
open class DuneNamedOwnerElementImpl(type: IElementType) : DuneElementImpl(type), DuneNamedOwnerElement {
    override fun getName(): String? = nameIdentifier?.text

    override fun setName(name: String): PsiElement = error("Operation Not Supported.")

    override fun getNameIdentifier(): PsiElement?
    = PsiTreeUtil.getChildOfType(this, DuneValue::class.java)?.let {
        it.atom ?: it.string ?: it.list?.value
    }
}