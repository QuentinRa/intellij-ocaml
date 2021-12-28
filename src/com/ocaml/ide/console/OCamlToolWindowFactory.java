package com.ocaml.ide.console;

import com.intellij.openapi.application.*;
import com.intellij.openapi.project.*;
import com.intellij.openapi.wm.*;
import icons.*;
import org.jetbrains.annotations.*;

public class OCamlToolWindowFactory implements ToolWindowFactory, DumbAware {

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
            OCamlConsoleRunner OCamlConsoleRunner = new OCamlConsoleRunner(project, window);
            OCamlConsoleRunner.runSync();
        });
    }
}