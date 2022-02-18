package com.ocaml.ide.console;

import com.intellij.execution.process.ProcessHandler;
import com.intellij.openapi.wm.ToolWindow;

public interface OCamlConsoleRunner {

    ToolWindow getWindow();
    ProcessHandler getProcessHandler();

    /**
     * @return true if running a command
     */
    boolean isRunning();
    void setRunning(boolean running);

    /**
     * Process a command, but it won't be added to the history
     *
     * @param s may be a whole file
     */
    void processCommand(String s);

    boolean isVariableViewEnabled();
    void showVariableView(boolean show);
    void rebuildVariableView(String text);
}
