package com.dune.ide.files;

import com.dune.DuneLanguage;
import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;

public class DuneFile extends PsiFileBase {
    public DuneFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, DuneLanguage.INSTANCE);
    }

    @NotNull @Override public FileType getFileType() {
        return DuneFileType.INSTANCE;
    }

    @NotNull @Override public String toString() {
        return DuneFileType.INSTANCE.getDescription();
    }
}
