package com.ocaml.ide.console;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.configurations.PtyCommandLine;
import com.intellij.execution.console.ConsoleHistoryController;
import com.intellij.execution.console.ProcessBackedConsoleExecuteActionHandler;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.AbstractConsoleRunnerWithHistory;
import com.intellij.execution.ui.RunContentDescriptor;
import com.intellij.ide.errorTreeView.NewErrorTreeViewPanel;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.actions.ScrollToTheEndToolbarAction;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.keymap.KeymapUtil;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.JBSplitter;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.util.ui.MessageCategory;
import com.ocaml.icons.OCamlIcons;
import com.ocaml.ide.console.actions.OCamlExecuteActionHandler;
import com.ocaml.ide.console.actions.OCamlRestartAction;
import com.ocaml.ide.console.debug.OCamlVariablesView;
import com.ocaml.sdk.utils.OCamlSdkCommandsManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Handle the console. The console is made of a process,
 * and we are sending commands to the process. It's quite tiring to use
 * for now, there is no history, and almost no features. It would be good if we
 * could see the current variables that we created for instance.
 */
public class OCamlConsoleRunner extends AbstractConsoleRunnerWithHistory<OCamlConsoleView> {

    private GeneralCommandLine commandLine;
    private Content myContent;
    private final ToolWindow myWindow;
    private OCamlVariablesView myVariablesView;

    public OCamlConsoleRunner(@NotNull Project project, @NotNull ToolWindow window) {
        super(project, OCamlConsoleView.CONS0LE_TITLE, null);
        myWindow = window;
    }

    // Console view
    @Override protected OCamlConsoleView createConsoleView() {
        return new OCamlConsoleView(getProject());
    }

    // Console icon
    @Override protected @Nullable Icon getConsoleIcon() {
        return OCamlIcons.Nodes.OCAML_CONSOLE;
    }

    // Interface
    @Override protected void createContentDescriptorAndActions() {
        // Tool Window with a toolbar + a main panel
        SimpleToolWindowPanel panel = new SimpleToolWindowPanel(false, true);

        // main panel
        OCamlConsoleView consoleView = getConsoleView();

        JBSplitter split = new JBSplitter(false, 0.5f);
        split.setFirstComponent(consoleView.getComponent());
        // create "variable view"
        myVariablesView = new OCamlVariablesView(consoleView);
        Disposer.register(consoleView, myVariablesView);
        split.setSecondComponent(myVariablesView);
        split.setShowDividerControls(true);
        split.setHonorComponentsMinimumSize(true);

        panel.setContent(split);

        // toolbar
        ActionToolbar toolbar = createToolbar(consoleView, panel);
        panel.setToolbar(toolbar.getComponent());

        // add
        myContent = ContentFactory.SERVICE.getInstance().createContent(panel, "", true);
        myWindow.getContentManager().addContent(myContent);
        Disposer.register(myWindow.getDisposable(), consoleView);
    }

    public void destroy() {
        // kill process
        ProcessHandler processHandler = getProcessHandler();
        if (processHandler != null) processHandler.destroyProcess();

        // remove content
        myWindow.getContentManager().removeContent(myContent, true);
    }

    // create the toolbar with some actions
    private @NotNull ActionToolbar createToolbar(@NotNull OCamlConsoleView console,
                                                 @NotNull SimpleToolWindowPanel mainPanel) {
        DefaultActionGroup group = new DefaultActionGroup();

        // add run actions
        ArrayList<AnAction> runActions = new ArrayList<>();
        runActions.add(createConsoleExecAction(getConsoleExecuteActionHandler()));
        runActions.add(new OCamlRestartAction(getProject()));
        runActions.add(Objects.requireNonNull(ConsoleHistoryController.getController(getConsoleView())).getBrowseHistory());
        group.addAll(runActions);

        // add other actions
        group.add(new ScrollToTheEndToolbarAction(console.getEditor()));

        registerActionShortcuts(runActions, console.getConsoleEditor().getComponent());
        registerActionShortcuts(runActions, mainPanel);

        // create toolbar
        ActionToolbar toolbar = ActionManager
                .getInstance()
                .createActionToolbar("left", group, false);
        toolbar.setTargetComponent(console.getComponent());

        return toolbar;
    }

    @Override
    protected AnAction createConsoleExecAction(@NotNull ProcessBackedConsoleExecuteActionHandler consoleExecuteActionHandler) {
        EditorEx consoleEditor = getConsoleView().getConsoleEditor();

        AnAction executeAction = super.createConsoleExecAction(consoleExecuteActionHandler);
        executeAction.registerCustomShortcutSet(CommonShortcuts.ENTER, consoleEditor.getComponent());

        String actionShortcutText = KeymapUtil.getFirstKeyboardShortcutText(executeAction);
        // todo: bundle
        consoleEditor.setPlaceholder("<"+actionShortcutText+"> to execute");
        consoleEditor.setShowPlaceholderWhenFocused(true);

        return executeAction;
    }

    // OCaml

    @Override protected @Nullable Process createProcess() throws ExecutionException {
        // create
        GeneralCommandLine cli = OCamlSdkCommandsManager.getREPLCommand(getProject());
        commandLine = new PtyCommandLine(cli).withInitialColumns(PtyCommandLine.MAX_COLUMNS);
        return commandLine.createProcess();
    }

    @Override protected OSProcessHandler createProcessHandler(Process process) {
        try {
            return new OCamlProcessHandler(commandLine, this, getConsoleView());
        } catch (ExecutionException e) {
            return null;
        }
    }

    @Override protected @NotNull ProcessBackedConsoleExecuteActionHandler createExecuteActionHandler() {
        ConsoleHistoryController historyController = new ConsoleHistoryController(OCamlConsoleRootType.getInstance(), "", getConsoleView());
        historyController.install();
        return new OCamlExecuteActionHandler(getProcessHandler(), true);
    }

    @Override public OCamlExecuteActionHandler getConsoleExecuteActionHandler() {
        return (OCamlExecuteActionHandler) super.getConsoleExecuteActionHandler();
    }

    // Run

    public void runSync() {
        try {
            initAndRun();
            // todo: bundle
            ProgressManager.getInstance().run(new Task.Backgroundable(getProject(),
                    "Connecting to Console", false) {
                @Override public void run(@NotNull ProgressIndicator indicator) {
                    // todo: bundle
                    indicator.setText("Connecting to console...");

                    OCamlConsoleView consoleView = OCamlConsoleRunner.this.getConsoleView();
                    consoleView.setup();
                }
            });
        } catch (Exception e) {
            showErrorsInConsole(e);
        }
    }

    private void showErrorsInConsole(Exception e) {
        var actionGroup = new DefaultActionGroup(new OCamlRestartAction(getProject()));

        var actionToolbar = ActionManager.getInstance()
                // todo: ???
                .createActionToolbar("OCamlConsoleErrors", actionGroup, false);

        // Runner creating
        var panel = new JPanel(new BorderLayout());
        panel.add(actionToolbar.getComponent(), BorderLayout.WEST);

        var errorViewPanel = new NewErrorTreeViewPanel(getProject(), null, false, false, null);
        actionToolbar.setTargetComponent(errorViewPanel);

        ArrayList<String> messages = new ArrayList<>();
        var message = e.getMessage();
        if (message != null && !message.isBlank()) {
            messages.addAll(message.lines().collect(Collectors.toList()));
        }

        errorViewPanel.addMessage(MessageCategory.ERROR, messages.toArray(new String[0]), null, -1, -1, null);
        panel.add(errorViewPanel, BorderLayout.CENTER);

        // todo: bundle
        var descriptor = new RunContentDescriptor(null, getProcessHandler(), panel, "Error Running REPL Console");
        Executor executor = getExecutor();

        // remove
        if (myContent != null) myWindow.getContentManager().removeContent(myContent, true);

        myContent = ContentFactory.SERVICE.getInstance().createContent(
                descriptor.getComponent(), descriptor.getDisplayName(), true
        );
        myContent.putUserData(ToolWindow.SHOW_CONTENT_ICON, java.lang.Boolean.TRUE);
        myContent.setIcon(descriptor.getIcon() == null ? executor.getToolWindowIcon() : descriptor.getIcon());
        myWindow.getContentManager().addContent(myContent);

        Disposer.register(myContent, errorViewPanel);
    }

    @Deprecated // we are not using the console, because we are not
    // showing the console in "run"
    @Override protected void showConsole(Executor defaultExecutor,
                                         @NotNull RunContentDescriptor contentDescriptor) {
    }

    // getters

    public ToolWindow getWindow() {
        return myWindow;
    }

    public OCamlVariablesView getVariablesView() {
        return myVariablesView;
    }

    /**
     * Process a command, but it won't be added to the history
     * @param s may be a whole file
     */
    public void processCommand(String s) {
        getConsoleExecuteActionHandler().processLine(s);
    }
}