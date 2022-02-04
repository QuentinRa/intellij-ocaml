package com.ocaml.ide.console.debug.groups;

public enum TreeElementGroupKind {
    // todo: bundle
    EXCEPTION("Exceptions"),
    MODULE("Modules"),
    TYPE("Types"),
    FUNCTIONS("Functions"),
    VARIABLES("Variables");

    public final String displayName;

    TreeElementGroupKind(String displayName) {
        this.displayName = displayName;
    }
}
