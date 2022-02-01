package com.ocaml.ide.actions.editor.run;

import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.vfs.VirtualFile;
import com.ocaml.OCamlBundle;
import com.ocaml.ide.actions.editor.OCamlEditorActionBase;
import com.ocaml.ide.console.OCamlConsoleRunner;
import com.ocaml.ide.console.OCamlConsoleToolWindowFactory;
import com.ocaml.utils.notifications.OCamlNotificationData;
import com.ocaml.utils.notifications.OCamlNotifications;
import org.jetbrains.annotations.NotNull;

public class OCamlRunFileREPLAction extends OCamlEditorActionBase {

    public static final String ACTION_ID = "editor.repl.run.action";

    @Override public void update(@NotNull AnActionEvent e) {
        super.update(e);
        VirtualFile data = e.getData(CommonDataKeys.VIRTUAL_FILE);
        if (data != null)
            e.getPresentation().setText(OCamlBundle.message("action.editor.repl.run.action", data.getName()));
    }

    @Override public void actionPerformed(@NotNull AnActionEvent e) {
        OCamlConsoleRunner runner = OCamlConsoleToolWindowFactory.getOCamlConsoleRunner();
        if (runner == null) {
            // show notification so that the user know that he/she must add sources manually
            var notification = new OCamlNotificationData(OCamlBundle.message("repl.no.started.desc"));
            notification.mySubtitle = OCamlBundle.message("repl.no.started.title");
            notification.myNotificationType = NotificationType.ERROR;
            OCamlNotifications.notify(notification);
            return;
        }
        VirtualFile file = e.getData(CommonDataKeys.VIRTUAL_FILE);
        if (file == null) return; // not happening
        var document = FileDocumentManager.getInstance().getDocument(file);
        if (document == null) return;
        String text = document.getText();
        runner.processCommand(text);
    }
}
