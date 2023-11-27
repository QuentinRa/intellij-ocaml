package com.ocaml.language.psi.api

import com.intellij.extapi.psi.StubBasedPsiElementBase
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.IndexSink
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.util.PsiTreeUtil
import com.ocaml.ide.files.OCamlLanguage

abstract class OCamlStubElementType<StubT : StubElement<*>, PsiT : OCamlElement>(
    debugName: String
) : IStubElementType<StubT, PsiT>(debugName, OCamlLanguage) {

    final override fun getExternalId(): String = "ocaml.${super.toString()}"

    override fun indexStub(stub: StubT, sink: IndexSink) {}
}

interface OCamlNamedStub {
    val name: String?
    val qualifiedName: String?
}

abstract class OCamlStubbedElementImpl<StubT : StubElement<*>> : StubBasedPsiElementBase<StubT>, OCamlElement {
    constructor(node: ASTNode) : super(node)
    constructor(stub: StubT, nodeType: IStubElementType<*, *>) : super(stub, nodeType)

    override fun toString(): String = "${javaClass.simpleName}($elementType)"
}

abstract class OCamlStubbedNamedElementImpl<StubT> : OCamlStubbedElementImpl<StubT>, OCamlNameIdentifierOwner
        where StubT : OCamlNamedStub, StubT : StubElement<*> {

    constructor(node: ASTNode) : super(node)

    constructor(stub: StubT, nodeType: IStubElementType<*, *>) : super(stub, nodeType)

    override fun getName(): String? {
        val stub = greenStub
        return if (stub !== null) stub.name else nameIdentifier?.text
    }

    override fun getQualifiedName(): String? {
        val stub = greenStub
        return if (stub !== null)
            stub.qualifiedName
        else {
            val ancestor = PsiTreeUtil.getParentOfType(this, OCamlQualifiedNamedElement::class.java)
            return if (ancestor == null) name
            else ancestor.qualifiedName + "." + name
        }
    }

    override fun setName(name: String): PsiElement {
        TODO("Not yet implemented")
    }
}