package com.ocaml.ide.structure

import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.util.treeView.smartTree.SortableTreeElement
import com.intellij.ide.util.treeView.smartTree.TreeElement
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.NavigatablePsiElement

class OCamlStructureViewElement(element: NavigatablePsiElement) : StructureViewTreeElement, SortableTreeElement {
    private val myElement: NavigatablePsiElement

    init {
        myElement = element
        println("PASS in for" + "myElement.text")
    }

    override fun getValue(): Any = myElement
    override fun navigate(requestFocus: Boolean) = myElement.navigate(requestFocus)
    override fun canNavigate(): Boolean = myElement.canNavigate()
    override fun canNavigateToSource(): Boolean = myElement.canNavigateToSource()
    override fun getAlphaSortKey(): String = myElement.name ?: ""
    override fun getPresentation(): ItemPresentation {
        if (myElement.presentation != null) return myElement.presentation!!
        val pres = PresentationData()
        pres.presentableText = myElement.name ?: "<unknown:${myElement.text.subSequence(0, 10)}>"
        return pres
    }

    override fun getChildren(): Array<out TreeElement> {
        // ex: show:OCaml Interface File<test.mli:>
        println("view:" + myElement + "<" + myElement.name + ">")

        val treeElements: MutableList<TreeElement> = mutableListOf()
//        if (myElement is OCamlFileBase) {
//            // ex: show:OCaml Interface File<test.mli:>
//            println("show:" + myElement + "<" + myElement.name + ">")
//            myElement.getAllVariables().forEach {
//                treeElements += OCamlStructureViewElement(it)
//                println("    found:" + it + "<" + it.elementType + ">")
//            }
//        }
        return if (treeElements.isEmpty()) StructureViewTreeElement.EMPTY_ARRAY else treeElements.toTypedArray()
    }
}
