package com.ocaml.ide.files;

import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.ocaml.lang.ocaml.OclLanguage;
import org.jetbrains.annotations.NotNull;

public class OclInterfaceFile extends FileBase {
  public OclInterfaceFile(@NotNull FileViewProvider viewProvider) {
    super(viewProvider, OclLanguage.INSTANCE);
  }

  @NotNull
  @Override
  public FileType getFileType() {
    return OclInterfaceFileType.INSTANCE;
  }

  @NotNull
  @Override
  public String toString() {
    return getName();
  }
}
