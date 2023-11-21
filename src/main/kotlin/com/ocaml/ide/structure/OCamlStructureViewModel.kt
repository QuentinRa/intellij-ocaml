package com.ocaml.ide.structure

import com.intellij.ide.structureView.StructureViewModel
import com.intellij.ide.structureView.StructureViewModelBase
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.util.treeView.smartTree.Filter
import com.intellij.ide.util.treeView.smartTree.Sorter
import com.intellij.openapi.editor.Editor
import com.ocaml.language.base.OCamlFileBase
import com.ocaml.language.psi.OCamlConstant

class OCamlStructureViewModel(editor: Editor?, psiFile: OCamlFileBase) :
    StructureViewModelBase(psiFile, editor, OCamlStructureViewElement(psiFile)),
    StructureViewModel.ElementInfoProvider {

    override fun getSorters(): Array<Sorter> = arrayOf(Sorter.ALPHA_SORTER)
    override fun getFilters(): Array<Filter> = super.getFilters()
    override fun isAlwaysShowsPlus(element: StructureViewTreeElement): Boolean = element.value is OCamlFileBase
    override fun isAlwaysLeaf(element: StructureViewTreeElement): Boolean = when (element.value) {
        is OCamlConstant -> true
        else -> false
    }
}
