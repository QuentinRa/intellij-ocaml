package com.ocaml.ide.sdk.library;

import com.intellij.openapi.roots.libraries.*;
import org.jetbrains.annotations.*;

public class OCamlLibraryKind extends PersistentLibraryKind<DummyLibraryProperties> {

    public static final OCamlLibraryKind INSTANCE = new OCamlLibraryKind();

    public OCamlLibraryKind() {
        super("OCaml");
    }

    @Override
    public @NotNull DummyLibraryProperties createDefaultProperties() {
        return DummyLibraryProperties.INSTANCE;
    }
}
