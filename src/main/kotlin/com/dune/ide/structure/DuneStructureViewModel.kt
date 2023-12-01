package com.dune.ide.structure

import com.dune.language.psi.files.DuneFile
import com.intellij.ide.structureView.StructureViewModel
import com.intellij.ide.structureView.StructureViewModelBase
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.util.treeView.smartTree.Filter
import com.intellij.ide.util.treeView.smartTree.Sorter
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile

class DuneStructureViewModel(editor: Editor?, psiFile: PsiFile) :
    StructureViewModelBase(psiFile, editor, DuneStructureViewElement(psiFile)),
    StructureViewModel.ElementInfoProvider {

    override fun getSorters(): Array<Sorter> = arrayOf(Sorter.ALPHA_SORTER)
    override fun getFilters(): Array<Filter> = super.getFilters()
    override fun isAlwaysShowsPlus(element: StructureViewTreeElement): Boolean = element.value is DuneFile
    override fun isAlwaysLeaf(element: StructureViewTreeElement): Boolean = false
}
