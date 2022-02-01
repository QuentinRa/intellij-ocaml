package com.ocaml.ide.highlight;

import com.intellij.openapi.compiler.CompilerManager;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.vfs.VirtualFile;
import com.ocaml.ide.files.FileHelper;

/**
 * Allow OCaml files to be underlined with a red underline, when there
 * is an error in the file.
 */
public class ErrorFileHighlighter implements Condition<VirtualFile> {

    private final Project myProject;

    public ErrorFileHighlighter(Project project) {
        myProject = project;
    }

    @Override public boolean value(VirtualFile virtualFile) {
        FileType fileType = virtualFile.getFileType();
        return FileHelper.isOCaml(fileType)
                && !CompilerManager.getInstance(myProject).isExcludedFromCompilation(virtualFile);
    }
}
