package com.ocaml.ide.files.editor;

import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorLocation;
import com.intellij.openapi.fileEditor.FileEditorState;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.util.UserDataHolderBase;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.beans.PropertyChangeListener;

/**
 * Add a tab preview, allowing the user
 * to preview the content of the file.
 */
public class OCamlAnnotFileEditor extends UserDataHolderBase implements FileEditor {

    private final VirtualFile file;
    private JPanel myMainPanel;

    public OCamlAnnotFileEditor(@NotNull VirtualFile file) {
        this.file = file;
    }

    @Override public @NotNull JComponent getComponent() {
        return myMainPanel;
    }

    @Override public @Nullable JComponent getPreferredFocusedComponent() {
        return myMainPanel;
    }

    @Override public @Nls(capitalization = Nls.Capitalization.Title) @NotNull String getName() {
        return "Preview";
    }

    @Override public void setState(@NotNull FileEditorState state) {
    }

    @Override public boolean isModified() {
        return false;
    }

    @Override public boolean isValid() {
        return true;
    }

    @Override public @Nullable FileEditorLocation getCurrentLocation() {
        return null;
    }

    @Override public void dispose() {
        Disposer.dispose(this);
    }

    @Override public void addPropertyChangeListener(@NotNull PropertyChangeListener listener) {
    }

    @Override public void removePropertyChangeListener(@NotNull PropertyChangeListener listener) {
    }

    // The default implementation of method 'getFile' is deprecated, you need to override it in
    // 'class com.ocaml.ide.files.editor.OCamlAnnotFileEditor'.
    // A proper @NotNull implementation required
    @Override public @Nullable VirtualFile getFile() {
        return file;
    }
}
