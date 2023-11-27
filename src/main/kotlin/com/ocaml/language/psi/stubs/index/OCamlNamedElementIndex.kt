package com.ocaml.language.psi.stubs.index

import com.intellij.psi.stubs.IndexSink
import com.intellij.psi.stubs.StringStubIndexExtension
import com.intellij.psi.stubs.StubIndexKey
import com.ocaml.language.psi.api.OCamlNamedElement
import com.ocaml.language.psi.files.OCamlFileStub

// Could be a variable/function, a type, a constructor, a module, a class
// Actually: only a variable
// StringIndex, should be int? (size--?)
// Scopes are not handled
class OCamlNamedElementIndex : StringStubIndexExtension<OCamlNamedElement>() {
    override fun getVersion(): Int = OCamlFileStub.Type.stubVersion
    override fun getKey(): StubIndexKey<String, OCamlNamedElement> = Constants.KEY

    private object Constants {
        val KEY: StubIndexKey<String, OCamlNamedElement> =
            StubIndexKey.createIndexKey("com.ocaml.index.OCamlNamedElementIndex")
    }

    object Utils {
        fun index(sink: IndexSink, key: String) {
            sink.occurrence(Constants.KEY, key)
        }
    }
}