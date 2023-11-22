package com.ocaml.language.psi.mixin

import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType
import com.ocaml.language.psi.OCamlLetBinding
import com.ocaml.language.psi.api.OCamlElementImpl

abstract class LetBindingMixin(type: IElementType) : OCamlElementImpl(type), OCamlLetBinding {
    override fun getNameIdentifier(): PsiElement? {
        return if(valueName != null)
            valueName
        else
            TODO("Not yet implemented")
    }

    override fun getName(): String? = nameIdentifier?.text

    override fun setName(name: String): PsiElement {
        TODO("Not yet implemented")
    }
}