package com.ocaml.ide.console;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.KillableColoredProcessHandler;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.util.Key;
import com.intellij.util.io.BaseOutputReader.Options;
import com.ocaml.sdk.repl.OCamlREPLConstants;
import org.jetbrains.annotations.NotNull;

/**
 * This class is updating the console view. We are not printing again the user input,
 * we are only printing the console output and updating the variables' view.
 */
public final class OCamlProcessHandler extends KillableColoredProcessHandler {
    private final OCamlConsoleRunner runner;
    private final OCamlConsoleView consoleView;

    public OCamlProcessHandler(@NotNull GeneralCommandLine commandLine,
                               @NotNull OCamlConsoleRunner runner,
                               @NotNull OCamlConsoleView consoleView) throws ExecutionException {
        super(commandLine);
        this.runner = runner;
        this.consoleView = consoleView;
        Disposer.register(this.consoleView, () -> {
            if (!isProcessTerminated()) destroyProcess();
        });
    }

    // this is the command that was used to start the console
    // we will not print it
    private boolean firstLine = false;
    private boolean waitForUserInput = true;
    public String output = "";

    public void coloredTextAvailable(@NotNull String textOriginal, @NotNull Key attributes) {
        if (!firstLine) { firstLine = true; return; }
        // pass empty
        if (textOriginal.isBlank()) return;
        // pass input (this is a trick, if the lines ends with ;;, then that's the user input)
        if (textOriginal.trim().endsWith(OCamlREPLConstants.END_LINE)) {
            waitForUserInput = false; // done
            output = "";
            return;
        }
        // can we pass the next command?
        if (textOriginal.replace(OCamlREPLConstants.PROMPT,"").trim().isEmpty()) {
            waitForUserInput = true; // wait again
            runner.setRunning(false); // yes
            // update variables view
            runner.rebuildVariableView(output);
            return;
        }
        if (waitForUserInput) return; // we are waiting

        // trim
        textOriginal = textOriginal.trim() + "\n";

        // merge texts
        output += textOriginal;

        // print
        consoleView.print(textOriginal, ConsoleViewContentType.SYSTEM_OUTPUT);
    }

    public boolean isSilentlyDestroyOnClose() {
        return true;
    }

    public boolean shouldKillProcessSoftly() {
        return true;
    }

    // If it's a long-running mostly idle daemon process,
    // consider overriding OSProcessHandler#readerOptions with
    // 'BaseOutputReader.Options.forMostlySilentProcess()' to reduce CPU usage.
    @NotNull
    protected Options readerOptions() {
        return Options.forMostlySilentProcess();
    }
}