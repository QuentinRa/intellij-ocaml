package com.ocaml.ide.console;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.KillableColoredProcessHandler;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.util.Key;
import com.intellij.util.io.BaseOutputReader.Options;
import org.jetbrains.annotations.NotNull;

public final class OCamlProcessHandler extends KillableColoredProcessHandler {
    private final OCamlConsoleRunner runner;
    private final OCamlConsoleView consoleView;

    public OCamlProcessHandler(@NotNull GeneralCommandLine commandLine,
                               @NotNull OCamlConsoleRunner runner,
                               @NotNull OCamlConsoleView consoleView) throws ExecutionException {
        super(commandLine);
        this.runner = runner;
        this.consoleView = consoleView;
        Disposer.register(this.consoleView, () -> { if (!isProcessTerminated()) destroyProcess(); });
    }

    private boolean processFirst = false;

    public void coloredTextAvailable(@NotNull String textOriginal, @NotNull Key attributes) {
        if (!processFirst) { processFirst = true; return; }
        if (textOriginal.isBlank()) return;
        if (textOriginal.trim().endsWith(";;")) { return; }

        // handle "val" -> variables or functions
        if (textOriginal.startsWith("val")) {
            if (runner.getVariablesView() != null) runner.getVariablesView().rebuild(textOriginal);
        }

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