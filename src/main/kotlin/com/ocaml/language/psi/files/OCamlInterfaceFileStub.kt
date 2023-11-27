package com.ocaml.language.psi.files

import com.intellij.psi.PsiFile
import com.intellij.psi.StubBuilder
import com.intellij.psi.impl.source.tree.TreeUtil
import com.intellij.psi.stubs.DefaultStubBuilder
import com.intellij.psi.stubs.PsiFileStubImpl
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.tree.IStubFileElementType
import com.ocaml.ide.files.OCamlInterfaceLanguage
import com.ocaml.ide.files.OCamlLanguage
import com.ocaml.language.base.OCamlBaseParserDefinition
import com.ocaml.language.psi.stubs.OCamlStubVersions

class OCamlInterfaceFileStub(file: OCamlInterfaceFile?) : PsiFileStubImpl<OCamlInterfaceFile>(file) {
    override fun getType() = Type
    object Type : IStubFileElementType<OCamlInterfaceFileStub>(OCamlInterfaceLanguage) {
        override fun getStubVersion(): Int = OCamlBaseParserDefinition.PARSER_VERSION + OCamlStubVersions.STUB_VERSION

        override fun getBuilder(): StubBuilder = object : DefaultStubBuilder() {
            override fun createStubForFile(file: PsiFile): StubElement<*> {
                TreeUtil.ensureParsed(file.node) // profiler hint
                check(file is OCamlInterfaceFile)
                return OCamlInterfaceFileStub(file)
            }
        }

        override fun getExternalId(): String = "ocaml.file.mli"
    }
}