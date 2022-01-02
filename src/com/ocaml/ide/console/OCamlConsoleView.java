package com.ocaml.ide.console;

import com.intellij.execution.console.*;
import com.intellij.openapi.application.*;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.editor.ex.*;
import com.intellij.openapi.project.*;
import com.reason.lang.ocaml.*;
import org.jetbrains.annotations.*;

public class OCamlConsoleView extends LanguageConsoleImpl {

    public OCamlConsoleView(@NotNull Project project) {
        super(project, "OCaml", OclLanguage.INSTANCE);

        ConsolePromptDecorator consolePromptDecorator = getConsolePromptDecorator();
        //consolePromptDecorator.setMainPrompt("#");
        //consolePromptDecorator.setIndentPrompt("  ");
        consolePromptDecorator.setMainPrompt("#");
        consolePromptDecorator.setIndentPrompt("  ");

        // ???
        setUpdateFoldingsEnabled(false);
    }

    public void setup() {
        ApplicationManager.getApplication().invokeLater(() -> {
            EditorEx consoleEditor = getConsoleEditor();
            Editor editor = getEditor();
            EditorEx historyViewer = getHistoryViewer();

            // remove borders
            historyViewer.setBorder(null);
            consoleEditor.setBorder(null);
            editor.setBorder(null);

            // enable wrap
            historyViewer.getSettings().setUseSoftWraps(true);
            consoleEditor.getSettings().setUseSoftWraps(true);
            editor.getSettings().setUseSoftWraps(true);
        });
    }
}
