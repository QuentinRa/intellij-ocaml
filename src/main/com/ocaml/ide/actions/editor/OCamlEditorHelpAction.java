package com.ocaml.ide.actions.editor;

import com.intellij.ide.browsers.BrowserLauncher;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class OCamlEditorHelpAction extends OCamlEditorActionBase  {

    public static final String ACTION_ID = "editor.help.action";

    @Override public void actionPerformed(@NotNull AnActionEvent e) {
        // before 11 (included)
        // https://ocaml.org/releases/4.11/htmlman/index.html
        // https://ocaml.org/releases/4.11/htmlman/libref/index_modules.html
        // after 11 (excluded)
        // https://ocaml.org/releases/4.12/manual/index.html
        // https://ocaml.org/releases/4.12/api/index.html
        String url = "https://ocaml.org/releases/4.12/manual/index.html";
        BrowserLauncher.getInstance().browse(url, null);
    }
}
