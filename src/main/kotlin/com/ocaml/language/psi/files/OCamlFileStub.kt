package com.ocaml.language.psi.files

import com.intellij.psi.PsiFile
import com.intellij.psi.StubBuilder
import com.intellij.psi.impl.source.tree.TreeUtil
import com.intellij.psi.stubs.DefaultStubBuilder
import com.intellij.psi.stubs.PsiFileStubImpl
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.tree.IStubFileElementType
import com.ocaml.ide.files.OCamlLanguage
import com.ocaml.language.parser.OCamlParserDefinition

class OCamlFileStub(file: OCamlFile?) : PsiFileStubImpl<OCamlFile>(file) {
    override fun getType() = Type

    object Type : IStubFileElementType<OCamlFileStub>(OCamlLanguage) {
        // Bump this number if Stub structure changes
        private const val STUB_VERSION = 2
        override fun getStubVersion(): Int = OCamlParserDefinition.PARSER_VERSION + STUB_VERSION

        override fun getBuilder(): StubBuilder = object : DefaultStubBuilder() {
            override fun createStubForFile(file: PsiFile): StubElement<*> {
                TreeUtil.ensureParsed(file.node) // profiler hint
                check(file is OCamlFile)
                return OCamlFileStub(file)
            }
        }

        override fun getExternalId(): String = "ocaml.file"
    }
}