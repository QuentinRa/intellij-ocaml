package com.ocaml.ide.sdk.library;

import com.intellij.openapi.project.*;
import com.intellij.openapi.roots.libraries.*;
import com.intellij.openapi.roots.libraries.ui.*;
import com.intellij.openapi.vfs.*;
import icons.*;
import org.jetbrains.annotations.*;

import javax.swing.*;

public class OCamlLibraryType extends LibraryType<DummyLibraryProperties> {
    public OCamlLibraryType() {
        super(OCamlLibraryKind.INSTANCE);
    }

    @Override
    public @NotNull String getCreateActionName() {
        return "OCaml";
    }

    @Override // todo: ignored?
    public @NotNull Icon getIcon(@Nullable DummyLibraryProperties properties) {
        return ORIcons.OCL_SDK;
    }

    @Override
    public @Nullable NewLibraryConfiguration createNewLibrary(@NotNull JComponent parentComponent, @Nullable VirtualFile contextDirectory, @NotNull Project project) {
        return null;
    }

    @Override
    public @Nullable LibraryPropertiesEditor createPropertiesEditor(@NotNull LibraryEditorComponent<DummyLibraryProperties> editorComponent) {
        return null;
    }
}
