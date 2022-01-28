package com.ocaml.ide.console.debug.groups.elements;

import com.intellij.icons.AllIcons;
import org.jetbrains.annotations.NotNull;

public class OCamlFunctionElement extends OCamlTreeElement {
    public OCamlFunctionElement(@NotNull String name, @NotNull String value, @NotNull String type) {
        super(name, value, type, AllIcons.Nodes.Method);
    }
}
