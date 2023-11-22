package com.or.ide.files;

import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.ocaml.OCamlLanguage;
import com.ocaml.ide.files.OCamlInterfaceFileType;
import org.jetbrains.annotations.NotNull;

public class OclInterfaceFile extends FileBase {
    public OclInterfaceFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, OCamlLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return OCamlInterfaceFileType.INSTANCE;
    }

    @NotNull
    @Override
    public String toString() {
        return getName();
    }
}
