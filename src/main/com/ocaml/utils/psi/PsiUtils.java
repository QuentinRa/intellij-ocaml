package com.ocaml.utils.psi;

import com.intellij.ide.util.*;
import com.intellij.openapi.project.*;
import com.intellij.openapi.vfs.*;

import java.io.*;

public final class PsiUtils {

    /** @see #openFile(Project, VirtualFile, boolean)  */
    public static void openFile(Project project, File file, boolean requestFocus) {
        VirtualFile virtualFile = LocalFileSystem.getInstance().findFileByIoFile(file);
        if (virtualFile == null) return;
        openFile(project, virtualFile, requestFocus);
    }

    /**
     * Open a file in the editor, may request focus
     * @param project the project
     * @param file the file
     * @param requestFocus true if we are requesting the focus.
     * @see PsiNavigationSupport
     */
    public static void openFile(Project project, VirtualFile file, boolean requestFocus) {
         var navigation = PsiNavigationSupport.getInstance();
         navigation.createNavigatable(project, file, -1).navigate(requestFocus);
    }

}
