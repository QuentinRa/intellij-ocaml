package com.reason.ide.files;

import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.vfs.*;
import com.intellij.psi.PsiFile;
import jpsplugin.com.reason.Platform;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileHelper {
    private FileHelper() {
    }

    public static boolean isOCaml(@Nullable FileType fileType) {
        return fileType instanceof OclFileType || fileType instanceof OclInterfaceFileType;
    }

    public static boolean isInterface(@Nullable FileType fileType) {
        return fileType instanceof OclInterfaceFileType;
    }
}
