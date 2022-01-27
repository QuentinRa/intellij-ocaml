package com.ocaml.ide.highlight;

import com.intellij.openapi.compiler.CompilerManager;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.vfs.VirtualFile;
import com.ocaml.ide.files.FileHelper;

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
