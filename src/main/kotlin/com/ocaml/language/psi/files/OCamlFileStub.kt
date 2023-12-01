package com.ocaml.language.psi.files

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiFile
import com.intellij.psi.StubBuilder
import com.intellij.psi.impl.source.tree.TreeUtil
import com.intellij.psi.stubs.DefaultStubBuilder
import com.intellij.psi.stubs.PsiFileStubImpl
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.IStubFileElementType
import com.ocaml.language.OCamlLanguage
import com.ocaml.language.base.OCamlBaseParserDefinition
import com.ocaml.language.psi.OCamlTypes
import com.ocaml.language.psi.stubs.OCamlStubVersions

class OCamlFileStub(file: OCamlFile?) : PsiFileStubImpl<OCamlFile>(file) {
    override fun getType() = Type

    object Type : IStubFileElementType<OCamlFileStub>(OCamlLanguage) {
        override fun getStubVersion(): Int = OCamlBaseParserDefinition.PARSER_VERSION + OCamlStubVersions.STUB_VERSION

        override fun getBuilder(): StubBuilder = object : DefaultStubBuilder() {
            override fun skipChildProcessingWhenBuildingStubs(parent: ASTNode, node: ASTNode): Boolean {
                return node.elementType == OCamlTypes.LET_BINDINGS
                        && parent.elementType !is IFileElementType
            }

            override fun createStubForFile(file: PsiFile): StubElement<*> {
                TreeUtil.ensureParsed(file.node) // profiler hint
                check(file is OCamlFile)
                return OCamlFileStub(file)
            }
        }

        override fun getExternalId(): String = "ocaml.file.ml"
    }
}