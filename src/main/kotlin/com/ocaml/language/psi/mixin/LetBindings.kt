package com.ocaml.language.psi.mixin

import com.intellij.psi.PsiElement
import com.intellij.psi.impl.ElementBase
import com.intellij.psi.tree.IElementType
import com.intellij.util.PlatformIcons
import com.ocaml.icons.OCamlIcons
import com.ocaml.language.psi.OCamlLetBinding
import com.ocaml.language.psi.api.OCamlElementImpl
import javax.swing.Icon

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

    // todo: ...
    override fun getIcon(flags: Int): Icon? {
        val visibilityIcon = PlatformIcons.PUBLIC_ICON
            // PlatformIcons.PUBLIC_ICON
            // PlatformIcons.PROTECTED_ICON
            // PlatformIcons.PRIVATE_ICON
        val icon = OCamlIcons.Nodes.LET
        return ElementBase.iconWithVisibilityIfNeeded(flags, icon, visibilityIcon)
    }
}