package com.ocaml.ide.console.actions;

import com.intellij.codeInsight.hint.HintManager;
import com.intellij.execution.console.ConsoleHistoryController;
import com.intellij.execution.console.LanguageConsoleView;
import com.intellij.execution.console.ProcessBackedConsoleExecuteActionHandler;
import com.ocaml.ide.console.OCamlConsoleRunner;
import com.ocaml.ide.console.OCamlConsoleView;
import com.ocaml.sdk.repl.OCamlREPLConstants;
import org.jetbrains.annotations.NotNull;

/**
 * OCaml command need ";;" at the end of each expression
 */
public class OCamlExecuteActionHandler extends ProcessBackedConsoleExecuteActionHandler {

    private final OCamlConsoleRunner runner;

    public OCamlExecuteActionHandler(@NotNull OCamlConsoleRunner runner, boolean preserveMarkup) {
        super(runner.getProcessHandler(), preserveMarkup);
        this.runner = runner;
    }

    @Override public void runExecuteAction(@NotNull LanguageConsoleView consoleView) {
        if (runner.isRunning()) {
            HintManager.getInstance().showErrorHint(
                    consoleView.getConsoleEditor(),
                    "Previous command is still running. Please wait, or restart the console."
            );
            return;
        }
        super.runExecuteAction(consoleView);
    }

    public void runExecuteAction(@NotNull  OCamlConsoleView consoleView, String text) {
        consoleView.printWithPrompt(text);
        ConsoleHistoryController.addToHistory(consoleView, text);
        execute(text, consoleView);
    }

    @Override public void processLine(@NotNull String line) {
        // appends ";;" if needed
        if (!line.isBlank() && !line.endsWith(OCamlREPLConstants.END_LINE)) line += OCamlREPLConstants.END_LINE;
        super.processLine(line);
    }
}
