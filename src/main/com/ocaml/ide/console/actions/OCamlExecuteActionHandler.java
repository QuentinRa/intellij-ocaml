package com.ocaml.ide.console.actions;

import com.intellij.execution.console.ProcessBackedConsoleExecuteActionHandler;
import com.intellij.execution.process.ProcessHandler;
import com.ocaml.sdk.repl.OCamlREPLConstants;
import org.jetbrains.annotations.NotNull;

/**
 * OCaml command need ";;" at the end of each expression
 */
public class OCamlExecuteActionHandler extends ProcessBackedConsoleExecuteActionHandler {

    public OCamlExecuteActionHandler(ProcessHandler processHandler, boolean preserveMarkup) {
        super(processHandler, preserveMarkup);
    }

    @Override public void processLine(@NotNull String line) {
        // appends ";;" if needed
        if (!line.isBlank() && !line.endsWith(OCamlREPLConstants.END_LINE)) line += OCamlREPLConstants.END_LINE;
        super.processLine(line);
    }
}
