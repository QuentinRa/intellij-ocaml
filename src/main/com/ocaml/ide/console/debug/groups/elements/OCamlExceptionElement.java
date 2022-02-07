package com.ocaml.ide.console.debug.groups.elements;

import com.ocaml.icons.OCamlIcons;
import org.jetbrains.annotations.NotNull;

public class OCamlExceptionElement extends OCamlTreeElement {
    public OCamlExceptionElement(@NotNull String name) {
        super(name, null, null, OCamlIcons.Nodes.EXCEPTION);
    }
}
