package com.ocaml.utils.editor;

import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.ocaml.lang.utils.OCamlPsiUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExtendedEditorActionUtil {

    /**
     * Return the selected code
     * @param editor the editor
     * @return null if we aren't in a situation in which we can find code to executed,
     * otherwise the selected code
     */
    public static @Nullable String getSelectedCode(@NotNull Editor editor) {
        // simply returns the selected code
        // user goal are beyond our understanding
        String code = editor.getSelectionModel().getSelectedText();
        if (code != null && !code.isBlank()) return code;
        // rip, we need to find what the user want to send to the console
        Project project = editor.getProject();
        if (project == null) return null;
        // find psiFile
        Document document = editor.getDocument();
        VirtualFile virtualFile = FileDocumentManager.getInstance().getFile(document);
        if (virtualFile == null) return null;
        PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(document);
        if (psiFile == null) return null;
        PsiElement elementAt = psiFile.findElementAt(editor.getCaretModel().getOffset());
        if (elementAt == null) return null;
        PsiElement s = OCamlPsiUtils.findStatementBefore(elementAt);
        if (s == null) {
            s = OCamlPsiUtils.findStatementAfter(elementAt);
            if (s == null) return null;
        }
        // select
        TextRange range = s.getNode().getTextRange();
        Caret currentCaret = editor.getCaretModel().getCurrentCaret();
        currentCaret.setSelection(range.getStartOffset(), range.getEndOffset());
        return s.getText();
    }
}
