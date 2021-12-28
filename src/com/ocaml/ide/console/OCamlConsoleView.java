package com.ocaml.ide.console;

import com.intellij.execution.console.*;
import com.intellij.execution.impl.*;
import com.intellij.execution.ui.*;
import com.intellij.openapi.project.*;
import com.ocaml.lang.ocaml.*;
import org.jetbrains.annotations.*;

public class OCamlConsoleView extends LanguageConsoleImpl implements ObservableConsoleView {

    public OCamlConsoleView(@NotNull Project project) {
        super(project, "OCaml", OclLanguage.INSTANCE);

        // Mark editor as console one, to prevent auto popup completion
        getHistoryViewer().putUserData(ConsoleViewUtil.EDITOR_IS_CONSOLE_HISTORY_VIEW, true);

        ConsolePromptDecorator consolePromptDecorator = getConsolePromptDecorator();
        //consolePromptDecorator.setMainPrompt("#");
        //consolePromptDecorator.setIndentPrompt("  ");
        consolePromptDecorator.setMainPrompt("");
        consolePromptDecorator.setIndentPrompt("");

        setUpdateFoldingsEnabled(false);
    }
}
