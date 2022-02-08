package com.ocaml.ide.console;

import com.intellij.execution.console.ConsoleHistoryCopyHandler;
import com.intellij.execution.console.ConsolePromptDecorator;
import com.intellij.execution.console.LanguageConsoleImpl;
import com.intellij.execution.impl.ConsoleViewUtil;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.execution.ui.ObservableConsoleView;
import com.intellij.ide.highlighter.HighlighterFactory;
import com.intellij.injected.editor.EditorWindow;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorSettings;
import com.intellij.openapi.editor.colors.EditorColors;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.ex.DocumentEx;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.ex.EditorGutterComponentEx;
import com.intellij.openapi.editor.ex.util.LexerEditorHighlighter;
import com.intellij.openapi.editor.highlighter.EditorHighlighter;
import com.intellij.openapi.editor.markup.HighlighterTargetArea;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiFile;
import com.ocaml.OCamlLanguage;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Handle how the console is rendered.
 */
public class OCamlConsoleView extends LanguageConsoleImpl implements ObservableConsoleView {

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

    /**
     * Print a command with the prompt, without changing the input text
     * @param command the command
     */
    public void printWithPrompt(String command) {
        ApplicationManager.getApplication().assertIsDispatchThread();

        EditorEx inputEditor = getCurrentEditor();
        String finalText;
        EditorHighlighter highlighter;
        if (inputEditor instanceof EditorWindow) {
            PsiFile file = ((EditorWindow)inputEditor).getInjectedFile();
            highlighter = HighlighterFactory.createHighlighter(file.getVirtualFile(), EditorColorsManager.getInstance().getGlobalScheme(), getProject());
            highlighter.setText(command);
            finalText = command;
        } else {
            finalText = command;
            highlighter = inputEditor.getHighlighter();
        }
        SyntaxHighlighter syntax = highlighter instanceof LexerEditorHighlighter ? ((LexerEditorHighlighter)highlighter).getSyntaxHighlighter() : null;

        doAddPromptToHistory();
        if (syntax != null) {
            ConsoleViewUtil.printWithHighlighting(this, finalText, syntax, () -> {
                String identPrompt = getConsolePromptDecorator().getIndentPrompt();
                if (StringUtil.isNotEmpty(identPrompt)) {
                    addPromptToHistoryImpl(identPrompt);
                }
            });
        }
        else {
            print(finalText, ConsoleViewContentType.USER_INPUT);
        }
        print("\n", ConsoleViewContentType.NORMAL_OUTPUT);
    }

    private void addPromptToHistoryImpl(@NotNull String prompt) {
        flushDeferredText();
        DocumentEx document = getHistoryViewer().getDocument();
        RangeHighlighter highlighter =
                this.getHistoryViewer().getMarkupModel().addRangeHighlighter(null, document.getTextLength(), document.getTextLength(), 0,
                        HighlighterTargetArea.EXACT_RANGE);
        //noinspection ConstantConditions
        print(prompt, getPromptAttributes());
        highlighter.putUserData(ConsoleHistoryCopyHandler.PROMPT_LENGTH_MARKER, prompt.length());
    }
}
