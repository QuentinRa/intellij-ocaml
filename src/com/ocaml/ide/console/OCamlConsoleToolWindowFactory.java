package com.ocaml.ide.console;

import com.intellij.openapi.application.*;
import com.intellij.openapi.project.*;
import com.intellij.openapi.wm.*;
import icons.*;
import org.jetbrains.annotations.*;

public class OCamlConsoleToolWindowFactory implements ToolWindowFactory, DumbAware {

    private static OCamlConsoleRunner myOCamlConsoleRunner;

    @Override
    public boolean shouldBeAvailable(@NotNull Project project) {
        return true;
    }

    @Override
    public void init(@NotNull ToolWindow window) {
        window.setIcon(ORIcons.OCAML);
        window.setTitle("Process");
        window.setStripeTitle("OCaml");
    }

    @Override
    public void createToolWindowContent(@NotNull final Project project, @NotNull ToolWindow window) {
        // Start
        ApplicationManager.getApplication().invokeLater(() -> {
            myOCamlConsoleRunner = new OCamlConsoleRunner(project, window);
            myOCamlConsoleRunner.runSync();
        });
    }

    public static void restartConsole(Project project) {
        if (myOCamlConsoleRunner != null){
            myOCamlConsoleRunner.destroy();
            // Start again
            ApplicationManager.getApplication().invokeLater(() -> {
                myOCamlConsoleRunner = new OCamlConsoleRunner(project, myOCamlConsoleRunner.myWindow);
                myOCamlConsoleRunner.runSync();
            });
        }
    }
}