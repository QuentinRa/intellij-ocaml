package com.ocaml.language.psi.stubs.index

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.impl.PsiDocumentManagerBase
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.stubs.IndexSink
import com.intellij.psi.stubs.StringStubIndexExtension
import com.intellij.psi.stubs.StubIndex
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

        fun findElementsByName(
            project: Project,
            target: String,
            scope: GlobalSearchScope = GlobalSearchScope.allScope(project)
        ): Collection<OCamlNamedElement> {
            checkCommitIsNotInProgress(project)
            return StubIndex.getElements(Constants.KEY, target, project, scope, OCamlNamedElement::class.java)
        }
    }
}

fun checkCommitIsNotInProgress(project: Project) {
    val app = ApplicationManager.getApplication()
    if ((app.isUnitTestMode || app.isInternal) && app.isDispatchThread) {
        if ((PsiDocumentManager.getInstance(project) as PsiDocumentManagerBase).isCommitInProgress) {
            error("Accessing indices during PSI event processing can lead to typing performance issues")
        }
    }
}