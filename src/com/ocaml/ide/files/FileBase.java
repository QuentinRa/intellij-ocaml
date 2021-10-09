package com.ocaml.ide.files;

import com.intellij.extapi.psi.*;
import com.intellij.lang.*;
import com.intellij.openapi.util.io.*;
import com.intellij.psi.*;
import org.jetbrains.annotations.*;

import java.util.*;

public abstract class FileBase extends PsiFileBase {
    private final @NotNull String m_moduleName;

    FileBase(@NotNull FileViewProvider viewProvider, @NotNull Language language) {
        super(viewProvider, language);
        m_moduleName = fileNameToModuleName(getName());
    }

    @NotNull
    public static String fileNameToModuleName(@NotNull String filename) {
        String nameWithoutExtension = FileUtilRt.getNameWithoutExtension(filename);
        if (nameWithoutExtension.isEmpty()) {
            return "";
        }
        return nameWithoutExtension.substring(0, 1).toUpperCase(Locale.getDefault())
                + nameWithoutExtension.substring(1);
    }

    public @NotNull String getModuleName() {
        return m_moduleName;
    }

    public boolean isInterface() {
        return FileHelper.isInterface(getFileType());
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FileBase fileBase = (FileBase) o;
        return m_moduleName.equals(fileBase.m_moduleName) && isInterface() == fileBase.isInterface();
    }

    @Override
    public int hashCode() {
        return Objects.hash(m_moduleName, isInterface());
    }
}
