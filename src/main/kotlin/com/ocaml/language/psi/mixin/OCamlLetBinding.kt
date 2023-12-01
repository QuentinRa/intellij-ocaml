package com.ocaml.language.psi.mixin

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.impl.ElementBase
import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.PlatformIcons
import com.ocaml.icons.OCamlIcons
import com.ocaml.language.psi.OCamlLetBinding
import com.ocaml.language.psi.OCamlValueName
import com.ocaml.language.psi.api.OCamlStubbedNamedElementImpl
import com.ocaml.language.psi.api.isAnonymous
import com.ocaml.language.psi.impl.OCamlLetBindingImpl
import com.ocaml.language.psi.stubs.OCamlLetBindingStub
import javax.swing.Icon

abstract class OCamlLetBindingMixin : OCamlStubbedNamedElementImpl<OCamlLetBindingStub>, OCamlLetBinding {
    constructor(node: ASTNode) : super(node)
    constructor(stub: OCamlLetBindingStub, nodeType: IStubElementType<*, *>) : super(stub, nodeType)

    override fun getNameIdentifier(): PsiElement? {
        return valueName?.firstChild?.firstChild
    }

    override fun getName(): String? {
       return super.getName() ?: computeValueNames().joinToString(",") { it.text }
    }

    override fun isFunction() : Boolean {
        // we should check the type as this check is not enough
        // but there is no type inference yet
        return getParameterList().isNotEmpty()
    }

    // PsiFile.LetBindings.<this>
    override fun isGlobal(): Boolean = parent.parent is PsiFile

    override fun getIcon(flags: Int): Icon? {
        val visibilityIcon = PlatformIcons.PUBLIC_ICON
        val icon = if (isFunction()) OCamlIcons.Nodes.FUNCTION else OCamlIcons.Nodes.LET
        return ElementBase.iconWithVisibilityIfNeeded(flags, icon, visibilityIcon)
    }
}

fun OCamlLetBinding.computeValueNames() : List<PsiElement> =
    PsiTreeUtil.findChildrenOfType(this, OCamlValueName::class.java).map {
        it.firstChild.firstChild
    }

fun expandLetBindingStructuredName(structuredName: String?) : List<String> {
    if (structuredName.isNullOrEmpty()) return listOf()
    if (!structuredName.contains(",")) return listOf(structuredName)
    val parts = structuredName.split(",").toMutableList()
    val prefix = parts[0].substringBeforeLast('.')
    parts[0] = parts[0].removePrefix("$prefix.")
    return parts.map { part -> "$prefix.$part" }
}

private class OCamlLetBindingDeconstruction(private val psi: PsiElement, private val letBinding: OCamlLetBinding) : OCamlLetBindingImpl(letBinding.node) {
    override fun getNameIdentifier(): PsiElement = psi
    override fun isFunction(): Boolean = false
    // Ensure TreeAnchorizer is still working as expected:
    override fun equals(other: Any?): Boolean = letBinding == other
    override fun hashCode(): Int = letBinding.hashCode()
}

fun handleStructuredLetBinding(letBinding: OCamlLetBinding): List<PsiElement> {
    if (letBinding.nameIdentifier == null) {
        // we are expanding children variable names
        return letBinding.computeValueNames().map {
            OCamlLetBindingDeconstruction(it, letBinding)
        }
    } else if (letBinding.isAnonymous()) {
        return listOf()
    }
    return listOf(letBinding)
}