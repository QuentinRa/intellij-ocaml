package com.dune.ide.structure;

import com.dune.ide.files.DuneFile;
import com.intellij.ide.structureView.StructureViewModel;
import com.intellij.ide.structureView.StructureViewModelBase;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.Filter;
import com.intellij.ide.util.treeView.smartTree.Sorter;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

public class DuneStructureViewModel extends StructureViewModelBase implements StructureViewModel.ElementInfoProvider {
    DuneStructureViewModel(@NotNull PsiFile psiFile) {
        super(psiFile, new DuneStructureViewElement(psiFile, 1));
    }

    public Sorter @NotNull [] getSorters() {
        return new Sorter[]{Sorter.ALPHA_SORTER};
    }

    @Override public Filter @NotNull [] getFilters() {
        return new Filter[]{new NestedFunctionsFilter()};
    }

    @Override public boolean isAlwaysShowsPlus(StructureViewTreeElement element) {
        return false;
    }

    @Override public boolean isAlwaysLeaf(StructureViewTreeElement element) {
        return element instanceof DuneFile;
    }
}
