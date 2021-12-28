package com.ocaml.ide.console;

import com.intellij.execution.*;
import com.intellij.execution.configurations.*;
import com.intellij.execution.console.*;
import com.intellij.execution.process.*;
import com.intellij.execution.runners.*;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.actions.*;
import com.intellij.openapi.editor.ex.*;
import com.intellij.openapi.keymap.*;
import com.intellij.openapi.progress.*;
import com.intellij.openapi.project.*;
import com.intellij.openapi.ui.*;
import com.intellij.openapi.util.*;
import com.intellij.openapi.wm.*;
import com.intellij.ui.content.*;
import com.ocaml.comp.opam.*;
import com.ocaml.ide.console.process.*;
import icons.*;
import org.jetbrains.annotations.*;

import javax.swing.*;
import java.util.*;

/**
 * Handle the console. The console is made of a process,
 * and we are sending commands to the process. It's quite tiring to use
 * for now, there is no history, and almost no features. It would be good if we
 * could see the current variables that we created for instance.
 *
 * todo: when switching ocaml version, reload the console
 * todo: allow the user to restart the console
 * todo: a table with the variables created and their values (as in the R plugin)
 * todo: handle errors (no System.out)
 * todo: make the console like in the R plugin, meaning that "we are directly interacting with
 *   the process".
 */
public class OCamlConsoleRunner extends AbstractConsoleRunnerWithHistory<OCamlConsoleView> {

    private final ToolWindow myWindow;
    private final GeneralCommandLine commandLine;

    public OCamlConsoleRunner(@NotNull Project project, @NotNull ToolWindow window) {
        super(project, "OCaml", null);
        myWindow = window;
        commandLine = getProject().getService(OpamService.class).ocaml();
    }

    // Console view
    @Override protected OCamlConsoleView createConsoleView() {
        return new OCamlConsoleView(getProject());
    }

    // Console icon
    @Override protected @Nullable Icon getConsoleIcon() {
        return ORIcons.OCAML;
    }

    // Interface
    @Override protected void createContentDescriptorAndActions() {
        // Tool Window with a toolbar + a main panel
        SimpleToolWindowPanel panel = new SimpleToolWindowPanel(false, true);

        // main panel
        OCamlConsoleView consoleView = getConsoleView();
        panel.setContent(consoleView.getComponent());

        // toolbar
        ActionToolbar toolbar = createToolbar(consoleView, panel);
        panel.setToolbar(toolbar.getComponent());

        // add
        Content content = ContentFactory.SERVICE.getInstance().createContent(panel, "", true);
        myWindow.getContentManager().addContent(content);
        Disposer.register(myWindow.getDisposable(), consoleView);
    }

    // create the toolbar with some actions
    private @NotNull ActionToolbar createToolbar(@NotNull OCamlConsoleView console,
                                                 @NotNull SimpleToolWindowPanel mainPanel) {
        DefaultActionGroup group = new DefaultActionGroup();

        // add run actions
        ArrayList<AnAction> runActions = new ArrayList<>();
        runActions.add(createConsoleExecAction(getConsoleExecuteActionHandler()));
        group.addAll(runActions);

        // add other actions
        group.add(new ScrollToTheEndToolbarAction(console.getEditor()));

        registerActionShortcuts(runActions, console.getConsoleEditor().getComponent());
        registerActionShortcuts(runActions, mainPanel);

        // create toolbar
        ActionToolbar toolbar = ActionManager.getInstance()
                .createActionToolbar("left", group, false);
        toolbar.setTargetComponent(console.getComponent());

        return toolbar;
    }

    @Override protected AnAction createConsoleExecAction(
            @NotNull ProcessBackedConsoleExecuteActionHandler consoleExecuteActionHandler) {
        EditorEx consoleEditor = getConsoleView().getConsoleEditor();

        AnAction executeAction = super.createConsoleExecAction(consoleExecuteActionHandler);
        executeAction.registerCustomShortcutSet(CommonShortcuts.ENTER, consoleEditor.getComponent());

        String actionShortcutText = KeymapUtil.getFirstKeyboardShortcutText(executeAction);
        consoleEditor.setPlaceholder("<"+actionShortcutText+"> to execute");
        consoleEditor.setShowPlaceholderWhenFocused(true);

        return executeAction;
    }

    // OCaml

    @Override protected @Nullable Process createProcess() throws ExecutionException {
        return commandLine.createProcess();
    }

    @Override protected OSProcessHandler createProcessHandler(Process process) {
        try {
            return new OSProcessHandler(commandLine);
        } catch (ExecutionException e) {
            return null;
        }
    }

    @Override protected @NotNull ProcessBackedConsoleExecuteActionHandler createExecuteActionHandler() {
        return new OCamlExecuteActionHandler(getProcessHandler(), true);
    }

    // Run

    public void runSync() {
        try {
            initAndRun();
            ProgressManager.getInstance().run(new Task.Backgroundable(getProject(),
                    "Connecting to Console", false) {
                @Override public void run(@NotNull ProgressIndicator indicator) {
                    indicator.setText("Connecting to console...");
                }
            });

            //getConsoleExecuteActionHandler().processLine("5;;");
        } catch (Exception e) {
            showErrorsInConsole(e);
        }
    }

    private void showErrorsInConsole(Exception e) {
        // todo: code
        System.out.println(e.getMessage());
    }
}