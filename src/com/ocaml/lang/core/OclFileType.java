package com.ocaml.lang.core;

import com.intellij.extapi.psi.*;
import com.intellij.openapi.fileTypes.*;
import com.intellij.psi.*;
import com.ocaml.lang.ocaml.*;
import org.jetbrains.annotations.*;

public class OclFileType extends PsiFileBase {
    public OclFileType(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, OclLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return com.ocaml.ide.files.OclFileType.INSTANCE;
    }
}
