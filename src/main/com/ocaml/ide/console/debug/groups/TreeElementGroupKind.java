package com.ocaml.ide.console.debug.groups;

import com.ocaml.OCamlBundle;
import org.jetbrains.annotations.NotNull;

// it's not safe to add a value here
// ensure that you know were they are used before adding one
// - removeElement
// - parser + tests
// - create an element in elements/
// - ShowElementGroupAction
// - OCamlVariablesView#rebuild
public enum TreeElementGroupKind {
    EXCEPTIONS(OCamlBundle.message("repl.variable.view.exceptions")),
    MODULES(OCamlBundle.message("repl.variable.view.modules")),
    TYPES(OCamlBundle.message("repl.variable.view.types")),
    FUNCTIONS(OCamlBundle.message("repl.variable.view.functions")),
    VARIABLES(OCamlBundle.message("repl.variable.view.variables"));

    public final String displayName;

    TreeElementGroupKind(@NotNull String name) {
        this.displayName = name;
    }
}
