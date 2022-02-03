package com.ocaml.ide.actions.editor;

import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.vfs.VirtualFile;
import com.ocaml.OCamlBundle;
import com.ocaml.ide.console.OCamlConsoleRunner;
import com.ocaml.ide.console.OCamlConsoleToolWindowFactory;
import com.ocaml.ide.files.OCamlFileType;
import com.ocaml.utils.notifications.OCamlNotificationData;
import com.ocaml.utils.notifications.OCamlNotifications;
import org.jetbrains.annotations.NotNull;

public abstract class OCamlEditorActionBase extends DumbAwareAction implements OCamlPromotedAction{

    public OCamlEditorActionBase() {
    }

    @Override public void update(@NotNull AnActionEvent e) {
        VirtualFile data = e.getData(CommonDataKeys.VIRTUAL_FILE);
        boolean ok = data != null && data.getFileType() == OCamlFileType.INSTANCE;

        Presentation presentation = e.getPresentation();
        presentation.setEnabled(e.isFromActionToolbar() || ok);
        presentation.setVisible(ok);
    }

    /**
     * Call doActionPerformed with a console, or show an error if we are not able
     * to find a console.
     * @param e see DumbAwareAction#actionPerformed(e)
     */
    @Override public void actionPerformed(@NotNull AnActionEvent e) {
        OCamlConsoleRunner runner = OCamlConsoleToolWindowFactory.getOCamlConsoleRunner();
        if (runner == null) {
            // show notification so that the user know that he/she must add sources manually
            var notification = new OCamlNotificationData(OCamlBundle.message("repl.no.started.desc"));
            notification.mySubtitle = OCamlBundle.message("repl.no.started.title");
            notification.myNotificationType = NotificationType.ERROR;
            OCamlNotifications.notify(notification);
        }
        doActionPerformed(e, runner);
    }

    protected void doActionPerformed(@NotNull AnActionEvent e, OCamlConsoleRunner runner) {

    }
}
