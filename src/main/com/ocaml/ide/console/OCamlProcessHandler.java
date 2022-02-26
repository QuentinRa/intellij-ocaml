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
    private boolean waitForEcho = false;
    public String output = "";
    public String potentialOutput = "";

    /**
     * We are expecting the output to be the command line ("firstLine"), that is skipped,
     * then we are repeating these
     * <ol>
     *     <li>#</li>
     *     <li>the input again (if we are in ocaml < 4.13), ending with ";;"</li>
     *     <li>the output of the command</li>
     * </ul>
     */
    public void coloredTextAvailable(@NotNull String textOriginal, @NotNull Key attributes) {
        if (!firstLine) { firstLine = true; return; }
        // pass empty (we got some empty lines in older versions)
        if (textOriginal.isBlank()) return;
        // did we get the echo of the user input?
        // (note that in 4.13+, we are never passing in this if,
        // as there is no echo of the input)
        if (isUserInputEchoReceived(textOriginal)) {
            waitForEcho = false; // no, let's start reading the output
            return;
        }
        // are we waiting for input?
        if (isPrompt(textOriginal)) {
            // yes, but are we done with the previous job?
            if (waitForEcho) { // no, transfer and print
                output = potentialOutput;
                consoleView.print(output, ConsoleViewContentType.SYSTEM_OUTPUT);
            }
            waitForEcho = true; // wait again
            runner.setRunning(false);
            // update variables view
            runner.rebuildVariableView(output);
            // clean
            output = "";
            potentialOutput = "";
            return;
        }

        // trim if not a suite of ^
        if (!textOriginal.trim().replace(OCamlREPLConstants.ERROR_INDICATOR,"").isBlank()) {
            textOriginal = textOriginal.trim() + "\n";
        }

        // issue 80: remove the leading stars
        // these are "returned" by ocaml for multilines comments
        while (textOriginal.startsWith("*")) {
            textOriginal = textOriginal.substring(1).stripLeading();
        }

        if (waitForEcho) {
            // we are waiting for the echo of the user input
            potentialOutput += textOriginal;
            return;
        }

        // merge texts
        output += textOriginal;

        // print
        consoleView.print(textOriginal, ConsoleViewContentType.SYSTEM_OUTPUT);
    }

    // shortcuts

    private boolean isUserInputEchoReceived(@NotNull String textOriginal) {
        return textOriginal.trim().endsWith(OCamlREPLConstants.END_LINE);
    }

    private boolean isPrompt(@NotNull String textOriginal) {
        return textOriginal.replace(OCamlREPLConstants.PROMPT,"")
                .replace(OCamlREPLConstants.PROMPT_COMMENT, "").trim().isEmpty();
    }

    // implementation

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