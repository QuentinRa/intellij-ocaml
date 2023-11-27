package com.ocaml.language.psi.mixin

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.ElementBase
import com.intellij.psi.stubs.IStubElementType
import com.intellij.util.PlatformIcons
import com.ocaml.icons.OCamlIcons
import com.ocaml.language.psi.OCamlImplUtils
import com.ocaml.language.psi.OCamlTypes
import com.ocaml.language.psi.OCamlValueDescription
import com.ocaml.language.psi.api.OCamlStubbedNamedElementImpl
import com.ocaml.language.psi.stubs.OCamlValBindingStub
import javax.swing.Icon

abstract class OCamlValBindingMixin : OCamlStubbedNamedElementImpl<OCamlValBindingStub>, OCamlValueDescription {
    constructor(node: ASTNode) : super(node)
    constructor(stub: OCamlValBindingStub, nodeType: IStubElementType<*, *>) : super(stub, nodeType)

    override fun getNameIdentifier(): PsiElement? {
        return valueName
    }

    override fun isFunction() : Boolean {
        // we should actually check the type using type inference
        return typexpr != null &&
                OCamlImplUtils.nextSiblingWithTokenType(typexpr!!.firstChild, OCamlTypes.RIGHT_ARROW) != null
    }

    override fun getIcon(flags: Int): Icon? {
        val visibilityIcon = PlatformIcons.PUBLIC_ICON
        val icon = if (isFunction()) OCamlIcons.Nodes.FUNCTION else OCamlIcons.Nodes.LET
        return ElementBase.iconWithVisibilityIfNeeded(flags, icon, visibilityIcon)
    }
}