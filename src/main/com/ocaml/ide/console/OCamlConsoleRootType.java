package com.ocaml.ide.console;

import com.intellij.execution.console.ConsoleRootType;
import com.intellij.ide.scratch.RootType;

public class OCamlConsoleRootType extends ConsoleRootType {

    public OCamlConsoleRootType() {
        super("ocaml", "OCaml console");
    }

    public static OCamlConsoleRootType getInstance() {
        return RootType.findByClass(OCamlConsoleRootType.class);
    }

}
