package com.or.ide.files;

import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.ocaml.OCamlLanguage;
import com.ocaml.ide.files.OCamlFileType;
import org.jetbrains.annotations.NotNull;

public class OclFile extends FileBase {
    public OclFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, OCamlLanguage.INSTANCE);
    }

    @Override
    public @NotNull FileType getFileType() {
        return OCamlFileType.INSTANCE;
    }

    @Override
    public @NotNull String toString() {
        return getName();
    }
}
