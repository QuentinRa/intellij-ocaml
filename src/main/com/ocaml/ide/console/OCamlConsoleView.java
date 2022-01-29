package com.ocaml.ide.console;

import com.intellij.execution.console.ConsolePromptDecorator;
import com.intellij.execution.console.LanguageConsoleImpl;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorSettings;
import com.intellij.openapi.editor.colors.EditorColors;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.ex.EditorGutterComponentEx;
import com.intellij.openapi.project.Project;
import com.ocaml.OCamlLanguage;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Handle how the console is rendered.
 */
public class OCamlConsoleView extends LanguageConsoleImpl {

    public static final String CONS0LE_TITLE = "OCaml";
    public static final String MAIN_PROMPT = ">>>";
    public static final String INDENT_PROMPT = ".. ";

    public OCamlConsoleView(@NotNull Project project) {
        super(project, CONS0LE_TITLE, OCamlLanguage.INSTANCE);

        ConsolePromptDecorator consolePromptDecorator = getConsolePromptDecorator();
        consolePromptDecorator.setMainPrompt(MAIN_PROMPT);
        consolePromptDecorator.setIndentPrompt(INDENT_PROMPT);
    }

    @Override protected @NotNull JComponent createCenterComponent() {
        JComponent centerComponent = super.createCenterComponent();
        EditorEx historyViewer = getHistoryViewer();
        EditorSettings settings = historyViewer.getSettings();
        settings.setAdditionalLinesCount(0);
        EditorEx consoleEditor = getConsoleEditor();
        EditorGutterComponentEx gutterComponentEx = consoleEditor.getGutterComponentEx();
        gutterComponentEx.setBackground(consoleEditor.getBackgroundColor());
        gutterComponentEx.revalidate();
        consoleEditor
                .getColorsScheme()
                .setColor(EditorColors.GUTTER_BACKGROUND, consoleEditor.getBackgroundColor());
        return centerComponent;
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
