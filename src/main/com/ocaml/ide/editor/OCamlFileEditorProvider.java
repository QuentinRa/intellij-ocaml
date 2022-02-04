package com.ocaml.ide.editor;

import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorPolicy;
import com.intellij.openapi.fileEditor.FileEditorProvider;
import com.intellij.openapi.fileEditor.impl.text.TextEditorImpl;
import com.intellij.openapi.fileEditor.impl.text.TextEditorProvider;
import com.intellij.openapi.fileTypes.FileTypeRegistry;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.vfs.VirtualFile;
import com.ocaml.ide.files.OCamlFileType;
import com.ocaml.ide.files.OCamlInterfaceFileType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class OCamlFileEditorProvider implements DumbAware, FileEditorProvider {

    @Override public @NotNull @NonNls String getEditorTypeId() {
        return "ocaml-editor";
    }

    @Override public boolean accept(@NotNull Project project, @NotNull VirtualFile file) {
        FileTypeRegistry fileTypeRegistry = FileTypeRegistry.getInstance();
        // not excluded, nor ignored, and in a source folder
        return ProjectFileIndex.getInstance(project).isInSourceContent(file)
                && // and either .ml or .mli
                (fileTypeRegistry.isFileOfType(file, OCamlFileType.INSTANCE)
                || fileTypeRegistry.isFileOfType(file, OCamlInterfaceFileType.INSTANCE));
    }

    @Override public @NotNull FileEditor createEditor(@NotNull Project project, @NotNull VirtualFile file) {
        TextEditorImpl editor = (TextEditorImpl) TextEditorProvider.getInstance().createEditor(project, file);
        return new OCamlFileEditor(project, editor, file);
    }

    @Override public @NotNull FileEditorPolicy getPolicy() {
        return FileEditorPolicy.HIDE_DEFAULT_EDITOR;
    }
}
