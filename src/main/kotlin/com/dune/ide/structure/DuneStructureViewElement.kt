package com.dune.ide.structure

import com.dune.ide.presentation.getPresentationForStructure
import com.dune.language.psi.DuneList
import com.dune.language.psi.files.DuneFile
import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.util.treeView.TreeAnchorizer
import com.intellij.ide.util.treeView.smartTree.TreeElement
import com.intellij.navigation.ItemPresentation
import com.intellij.openapi.ui.Queryable
import com.intellij.pom.Navigatable
import com.intellij.psi.PsiElement
import com.intellij.psi.util.childrenOfType
import com.intellij.util.containers.map2Array

class DuneStructureViewElement(element: PsiElement) : StructureViewTreeElement, Queryable {
    private val psiAnchor = TreeAnchorizer.getService().createAnchor(element)
    private val myElement: PsiElement? get() = TreeAnchorizer.getService().retrieveElement(psiAnchor) as? PsiElement
    private val childElements: List<PsiElement>
        get() {
            return when (val psi = myElement) {
                is DuneFile -> psi.childrenOfType<DuneList>().filter { it.value?.atom != null }
                // [HUMAN]: we basically only keep the lists that have an atom
                // So the structure view only display lists (name=the atom) and their sub-lists
                // (while repeating the filtering/display process)
                is DuneList -> psi.argumentList.filter { it.list?.value?.atom != null }.map {
                    it.list!!
                }
                else -> emptyList()
            }
        }

    override fun getValue(): PsiElement? = myElement
    override fun navigate(requestFocus: Boolean) {
        (myElement as? Navigatable)?.navigate(requestFocus)
    }

    override fun canNavigate(): Boolean = (myElement as? Navigatable)?.canNavigate() == true
    override fun canNavigateToSource(): Boolean = (myElement as? Navigatable)?.canNavigateToSource() == true
    override fun getPresentation(): ItemPresentation {
        return myElement?.let(::getPresentationForStructure) ?: PresentationData("unknown", null, null, null)
    }

    override fun getChildren(): Array<out TreeElement> = childElements.map2Array { DuneStructureViewElement(it) }

    override fun putInfo(info: MutableMap<in String, in String>) {
        val presentation = presentation
        info["name"] = presentation.presentableText ?: ""
    }
}