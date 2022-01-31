package com.intellij.openapi.actionSystem.ex;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class ActionUtilsAdaptor {

    public static void performActionDumbAwareWithCallbacks(@NotNull AnAction action, @NotNull AnActionEvent e) {
        ActionUtil.performActionDumbAwareWithCallbacks(action, e);
    }

}
