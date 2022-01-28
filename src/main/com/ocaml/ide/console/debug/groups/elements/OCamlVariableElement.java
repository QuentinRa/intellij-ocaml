package com.ocaml.ide.console.debug.groups.elements;

import com.intellij.icons.AllIcons;
import org.jetbrains.annotations.NotNull;

public class OCamlVariableElement extends OCamlTreeElement {
    public OCamlVariableElement(@NotNull String name, @NotNull String value, @NotNull String type) {
        super(name, value, type, AllIcons.Nodes.Variable);
    }
}
