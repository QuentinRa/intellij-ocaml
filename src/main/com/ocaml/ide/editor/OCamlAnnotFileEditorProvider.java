package com.ocaml.ide.editor;

import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorPolicy;
import com.intellij.openapi.fileEditor.FileEditorProvider;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.ocaml.ide.files.OCamlAnnotFileType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class OCamlAnnotFileEditorProvider implements FileEditorProvider, DumbAware {

    private static final String EDITOR_TYPE_ID = "OCAML_ANNOT";

    @Override public boolean accept(@NotNull Project project, @NotNull VirtualFile file) {
        return file.getFileType() == OCamlAnnotFileType.INSTANCE;
    }

    @Override public @NotNull FileEditor createEditor(@NotNull Project project, @NotNull VirtualFile file) {
        return new OCamlAnnotFileEditor(file);
    }

    @Override public @NotNull @NonNls String getEditorTypeId() {
        return EDITOR_TYPE_ID;
    }

    // note: must be DumbAware, or it will not work as expected
    @Override public @NotNull FileEditorPolicy getPolicy() {
        return FileEditorPolicy.PLACE_BEFORE_DEFAULT_EDITOR;
    }
}
