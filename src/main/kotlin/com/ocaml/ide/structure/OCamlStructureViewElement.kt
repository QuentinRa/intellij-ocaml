package com.ocaml.ide.structure

import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.util.treeView.TreeAnchorizer
import com.intellij.ide.util.treeView.smartTree.TreeElement
import com.intellij.navigation.ItemPresentation
import com.intellij.pom.Navigatable
import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.PsiElement
import com.intellij.psi.util.childrenOfType
import com.intellij.util.containers.map2Array
import com.ocaml.language.psi.OCamlLetBindings
import com.ocaml.language.psi.files.OCamlFile

class OCamlStructureViewElement(element: PsiElement) : StructureViewTreeElement {
    private val psiAnchor = TreeAnchorizer.getService().createAnchor(element)
    private val myElement: PsiElement? get() = TreeAnchorizer.getService().retrieveElement(psiAnchor) as? PsiElement
    private val childElements: List<PsiElement>
        get() {
            return when (val psi = myElement) {
                is OCamlFile -> {
                    psi.childrenOfType<OCamlLetBindings>().map {
                        val allBindings = it.letBindingList
                        if (allBindings.size == 1)
                            allBindings[0] as PsiElement
                        else
                            it
                    }
                }
                is OCamlLetBindings -> psi.letBindingList
                else -> emptyList()
            }
        }

    override fun getValue(): PsiElement? = myElement
    override fun navigate(requestFocus: Boolean) { (myElement as? Navigatable)?.navigate(requestFocus) }
    override fun canNavigate(): Boolean = (myElement as? Navigatable)?.canNavigate() == true
    override fun canNavigateToSource(): Boolean = (myElement as? Navigatable)?.canNavigateToSource() == true
    override fun getPresentation(): ItemPresentation {
        myElement?.let {
            val psi = myElement as? NavigatablePsiElement
            psi?.let {
                if (psi.presentation != null) return psi.presentation!!
                val pres = PresentationData()
                pres.presentableText = psi.name ?: "<unknown:${psi.text.subSequence(0, 10)}>"
                return pres
            }
        }
        return  PresentationData("unknown", "", null, null)
    }
    override fun getChildren(): Array<out TreeElement> =
        childElements.map2Array { OCamlStructureViewElement(it) }
}
