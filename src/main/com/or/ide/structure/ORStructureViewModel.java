package com.or.ide.structure;

import com.intellij.ide.structureView.StructureViewModel;
import com.intellij.ide.structureView.StructureViewModelBase;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.Filter;
import com.intellij.ide.util.treeView.smartTree.Sorter;
import com.intellij.psi.PsiFile;
import com.or.ide.files.OclFile;
import org.jetbrains.annotations.NotNull;

public class ORStructureViewModel extends StructureViewModelBase implements StructureViewModel.ElementInfoProvider {
    ORStructureViewModel(@NotNull PsiFile psiFile) {
        super(psiFile, new StructureViewElement(psiFile, 1));
    }

    public Sorter [] getSorters() {
        return new Sorter[]{Sorter.ALPHA_SORTER};
    }

    @Override
    public Filter [] getFilters() {
        return new Filter[]{new NestedFunctionsFilter()};
    }

    @Override
    public boolean isAlwaysShowsPlus(StructureViewTreeElement element) {
        return false;
    }

    @Override
    public boolean isAlwaysLeaf(StructureViewTreeElement element) {
        return element instanceof OclFile;
    }
}
