package com.ocaml.ide.highlight;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.vfs.VirtualFile;
import com.ocaml.ide.files.FileHelper;
import com.ocaml.utils.sdk.OCamlSdkUtils;

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
        return FileHelper.isOCaml(virtualFile) && OCamlSdkUtils.isNotExcluded(myProject, virtualFile);
    }
}
