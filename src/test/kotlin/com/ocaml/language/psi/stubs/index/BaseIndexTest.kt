package com.ocaml.language.psi.stubs.index

import com.intellij.psi.PsiElement
import com.intellij.psi.stubs.IndexSink
import com.intellij.psi.stubs.StubIndexKey
import com.ocaml.ide.OCamlBasePlatformTestCase
import com.ocaml.language.psi.api.OCamlNamedElement
import com.ocaml.language.psi.files.OCamlFileStub

interface StubIndexForTests {
    /**
     * Number of element indexed
     */
    var total : Int
    /**
     * Each name is mapped to its number of occurrences
     */
    val namedIndexValuesCount : MutableMap<String, Int>
}

abstract class BaseIndexTest : OCamlBasePlatformTestCase() {


    private class FakeStubSinkIndex : IndexSink, StubIndexForTests {
        /**
         * Number of element indexed
         */
        override var total : Int = 0

        /**
         * Each name is mapped to its number of occurrences
         */
        override val namedIndexValuesCount : MutableMap<String, Int> = HashMap()

        override fun <Psi : PsiElement?, K : Any?> occurrence(indexKey: StubIndexKey<K, Psi>, value: K & Any) {
            @Suppress("UNCHECKED_CAST")
            val namedIndex = indexKey as? StubIndexKey<String, OCamlNamedElement> ?: return
            val indexValue = value as? String ?: return
            val count = namedIndexValuesCount[indexValue] ?: 0
            namedIndexValuesCount[indexValue] = count +1
            total++
        }
    }

    protected fun testIndex(filename: String, code: String) : StubIndexForTests {
        val file = configureCode(filename, code)
        val stubTree = OCamlFileStub.Type.builder.buildStubTree(file)
        val indexSink = FakeStubSinkIndex()
        stubTree.childrenStubs.forEach { it.stubType.indexStub(it, indexSink) }
        return indexSink
    }
}