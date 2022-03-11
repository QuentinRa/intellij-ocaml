package com.ocaml.ide.editor;

import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorPolicy;
import com.intellij.openapi.fileEditor.FileEditorProvider;
import com.intellij.openapi.fileEditor.impl.text.TextEditorImpl;
import com.intellij.openapi.fileEditor.impl.text.TextEditorProvider;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.ocaml.ide.files.FileHelper;
import com.ocaml.utils.files.OCamlProjectFilesUtils;
import com.ocaml.utils.sdk.OCamlSdkUtils;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class OCamlFileEditorProvider implements DumbAware, FileEditorProvider {

    @Override public @NotNull @NonNls String getEditorTypeId() {
        return "ocaml-editor";
    }

    @Override public boolean accept(@NotNull Project project, @NotNull VirtualFile file) {
        return FileHelper.isOCaml(file)
                && OCamlProjectFilesUtils.isInProject(project, file)
                && OCamlSdkUtils.isNotExcluded(project, file);
    }

    @Override public @NotNull FileEditor createEditor(@NotNull Project project, @NotNull VirtualFile file) {
        TextEditorImpl editor = (TextEditorImpl) TextEditorProvider.getInstance().createEditor(project, file);
        return new OCamlFileEditor(project, editor, file);
    }

    @Override public @NotNull FileEditorPolicy getPolicy() {
        return FileEditorPolicy.HIDE_DEFAULT_EDITOR;
    }
}
