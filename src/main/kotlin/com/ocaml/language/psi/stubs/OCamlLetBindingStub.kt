package com.ocaml.language.psi.stubs

import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.*
import com.ocaml.language.psi.OCamlLetBinding
import com.ocaml.language.psi.api.OCamlNamedStub
import com.ocaml.language.psi.api.OCamlStubElementType
import com.ocaml.language.psi.api.isAnonymous
import com.ocaml.language.psi.createStubIfParentIsStub
import com.ocaml.language.psi.impl.OCamlLetBindingImpl
import com.ocaml.language.psi.mixin.expandLetBindingStructuredName
import com.ocaml.language.psi.stubs.index.OCamlNamedElementIndex

class OCamlLetBindingStub(
    parent: StubElement<*>?,
    elementType: IStubElementType<*, *>,
    override val name: String?,
    override val qualifiedName: String?
) : StubBase<OCamlLetBinding>(parent, elementType), OCamlNamedStub {

    object Type : OCamlStubElementType<OCamlLetBindingStub, OCamlLetBinding>("LET_BINDING") {
        override fun shouldCreateStub(node: ASTNode): Boolean = createStubIfParentIsStub(node)

        override fun createPsi(stub: OCamlLetBindingStub) = OCamlLetBindingImpl(stub, this)

        override fun createStub(psi: OCamlLetBinding, parentStub: StubElement<*>?) =
            OCamlLetBindingStub(parentStub, this, psi.name, if (psi.isAnonymous()) null else psi.qualifiedName)

        override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?) = OCamlLetBindingStub(
            parentStub, this, dataStream.readName()?.string, dataStream.readName()?.string
        )

        override fun serialize(stub: OCamlLetBindingStub, dataStream: StubOutputStream) = with(dataStream) {
            writeName(stub.name)
            writeName(stub.qualifiedName)
        }

        override fun indexStub(stub: OCamlLetBindingStub, sink: IndexSink) {
            expandLetBindingStructuredName(stub.qualifiedName)
                .forEach { OCamlNamedElementIndex.Utils.index(sink, it) }
        }
    }
}