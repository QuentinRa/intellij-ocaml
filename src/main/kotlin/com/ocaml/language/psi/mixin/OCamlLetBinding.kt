package com.ocaml.language.psi.mixin

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.ElementBase
import com.intellij.psi.stubs.IStubElementType
import com.intellij.util.PlatformIcons
import com.ocaml.icons.OCamlIcons
import com.ocaml.language.psi.OCamlLetBinding
import com.ocaml.language.psi.api.OCamlStubbedNamedElementImpl
import com.ocaml.language.psi.stubs.OCamlLetBindingStub
import javax.swing.Icon

abstract class OCamlLetBindingMixin : OCamlStubbedNamedElementImpl<OCamlLetBindingStub>, OCamlLetBinding {
    constructor(node: ASTNode) : super(node)
    constructor(stub: OCamlLetBindingStub, nodeType: IStubElementType<*, *>) : super(stub, nodeType)

    // todo: add tests
    //   - let x = 5
    //   - let x,y = 5,6
    //   - let (x,y) = 5,6
    override fun getNameIdentifier(): PsiElement? {
        return if(valueName != null)
            valueName
        else if (patternNoExn != null) {
            patternNoExn!!.patternExpression
        } else
            TODO("Not yet implemented for:$text")
    }

    override fun getName(): String? {
        val stub = greenStub
        return if (stub !== null) stub.name else nameIdentifier?.text
    }

    override fun setName(name: String): PsiElement {
        TODO("Not yet implemented")
    }

    override fun isFunction() : Boolean {
        // actually, we should check the type, but well...
        return getParameterList().isNotEmpty()
    }

    // todo: ...
    override fun getIcon(flags: Int): Icon? {
        val visibilityIcon = PlatformIcons.PUBLIC_ICON
            // PlatformIcons.PUBLIC_ICON
            // PlatformIcons.PROTECTED_ICON
            // PlatformIcons.PRIVATE_ICON
        val icon = if (isFunction()) OCamlIcons.Nodes.FUNCTION else OCamlIcons.Nodes.LET
        return ElementBase.iconWithVisibilityIfNeeded(flags, icon, visibilityIcon)
    }
}