package com.ocaml.ide.console;

import com.intellij.execution.*;
import com.intellij.execution.configurations.*;
import com.intellij.execution.process.*;
import com.intellij.execution.ui.*;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.util.Key;
import com.intellij.util.io.BaseOutputReader.Options;

import org.jetbrains.annotations.NotNull;

public final class OCamlProcessHandler extends KillableColoredProcessHandler {
    private final OCamlConsoleView consoleView;

    public OCamlProcessHandler(@NotNull GeneralCommandLine commandLine,
                                   @NotNull OCamlConsoleView consoleView) throws ExecutionException {
        super(commandLine);

        this.consoleView = consoleView;
        Disposer.register(this.consoleView, () -> { if (!isProcessTerminated()) destroyProcess(); });
    }

    public void coloredTextAvailable(@NotNull String textOriginal, @NotNull Key attributes) {
        ConsoleViewContentType type;

        if (attributes == ProcessOutputTypes.STDERR) {
            type = ConsoleViewContentType.ERROR_OUTPUT;
        } else if (attributes == ProcessOutputTypes.SYSTEM) {
            type = ConsoleViewContentType.SYSTEM_OUTPUT;
        } else {
            type = ConsoleViewContentType.getConsoleViewType(attributes);
        }

        consoleView.print(textOriginal, type);
    }

    public boolean isSilentlyDestroyOnClose() { return true; }

    public boolean shouldKillProcessSoftly() {
        return true;
    }

    @NotNull
    protected Options readerOptions() {
        return Options.forMostlySilentProcess();
    }
}