package com.ocaml.ide.actions.editor.run;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.ocaml.OCamlBundle;
import com.ocaml.ide.actions.editor.OCamlEditorActionBase;
import com.ocaml.ide.console.OCamlConsoleRunner;
import com.ocaml.lang.utils.OCamlElementVisitor;
import com.or.ide.files.FileBase;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Send the whole file to the REPL
 */
public class OCamlRunFileREPLAction extends OCamlEditorActionBase {

    public static final String ACTION_ID = "editor.repl.run.action";

    public static void doAction(@NotNull Editor editor, Project project, OCamlConsoleRunner runner) {
        // find psiFile
        Document document = editor.getDocument();
        PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(document);
        if (psiFile == null) return;
        // process each statement
        if (psiFile instanceof FileBase) {
            ArrayList<PsiElement> elements = new ArrayList<>();
            psiFile.acceptChildren(new OCamlElementVisitor(elements, 1));
            for (PsiElement element : elements) {
                runner.processCommand(element.getText());
            }
        } else {
            // process the whole file in one go
            String text = document.getText();
            runner.processCommand(text);
        }
    }

    @Override public void update(@NotNull AnActionEvent e) {
        super.update(e);
        VirtualFile data = e.getData(CommonDataKeys.VIRTUAL_FILE);
        if (data != null)
            e.getPresentation().setText(OCamlBundle.message("action.editor.repl.run.action", data.getName()));
    }

    @Override protected void doActionPerformed(@NotNull AnActionEvent e, OCamlConsoleRunner runner) {
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        if (editor == null) return;
        Project project = e.getProject();
        if (project == null) return;
        doAction(editor, project, runner);
    }
}
