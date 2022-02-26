package com.ocaml.ide.console;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.ocaml.icons.OCamlIcons;
import org.jetbrains.annotations.NotNull;

/**
 * Should handle the creation of windows.
 */
public class OCamlConsoleToolWindowFactory implements ToolWindowFactory, DumbAware {

    private static OCamlConsoleRunnerImpl myOCamlConsoleRunner;

    public static OCamlConsoleRunner getOCamlConsoleRunner() {
        return myOCamlConsoleRunner;
    }

    @Override
    public boolean shouldBeAvailable(@NotNull Project project) {
        return true;
    }

    @Override
    public void init(@NotNull ToolWindow window) {
        window.setIcon(OCamlIcons.Nodes.OCAML_CONSOLE);
        window.setTitle("");
        window.setStripeTitle(OCamlConsoleView.CONS0LE_TITLE);
    }

    @Override
    public void createToolWindowContent(@NotNull final Project project, @NotNull ToolWindow window) {
        // Start
        ApplicationManager.getApplication().invokeLater(() -> {
            myOCamlConsoleRunner = new OCamlConsoleRunnerImpl(project, window);
            myOCamlConsoleRunner.runSync();
        });
    }

    public static void restartConsole(Project project) {
        if (myOCamlConsoleRunner != null){
            myOCamlConsoleRunner.destroy();
            // Start again
            ApplicationManager.getApplication().invokeLater(() -> {
                myOCamlConsoleRunner = new OCamlConsoleRunnerImpl(project, myOCamlConsoleRunner.getWindow());
                myOCamlConsoleRunner.runSync();
            });
        }
    }
}