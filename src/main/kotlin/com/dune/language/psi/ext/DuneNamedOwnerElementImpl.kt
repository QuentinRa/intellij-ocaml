package com.dune.language.psi.ext

import com.dune.language.psi.*
import com.dune.language.psi.api.DuneElementImpl
import com.dune.language.psi.api.DuneNamedElement
import com.dune.language.psi.api.DuneNamedOwnerElement
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType
import com.intellij.psi.util.PsiTreeUtil

// Base class for DuneNamedElement elements
open class DuneNamedElementImpl(type: IElementType) : DuneElementImpl(type), DuneNamedElement {
    override fun getName(): String? = findPsiChildByType(DuneTypes.ATOM_VALUE_ID)?.text
    override fun setName(name: String): PsiElement? {
        val newElement = DunePsiFactory.createNamedAtom(project, name)
        return replace(newElement)
    }
}

// Base class for DuneNamedOwnerElement elements
open class DuneNamedOwnerElementImpl(type: IElementType) : DuneElementImpl(type), DuneNamedOwnerElement {
    override fun getName(): String? = (nameIdentifier as? DuneNamedElement)?.name
    override fun setName(name: String): PsiElement? {
        return (nameIdentifier as? DuneNamedElement)?.setName(name)
    }
    override fun getNameIdentifier(): PsiElement?
        = PsiTreeUtil.getChildOfType(this, DuneValue::class.java)?.atom?.namedAtom
}