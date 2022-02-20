package com.ocaml.utils.editor;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.ocaml.lang.core.PsiLetWithAnd;
import com.ocaml.lang.utils.OCamlPsiUtils;
import com.ocaml.utils.Tested;
import com.or.lang.core.psi.PsiStructuredElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

@Tested(note = "I'm directly testing the class using this one. If I ever have to fix a problem " +
        "in this class again, I will add tests (note to myself).")
public class ExtendedEditorActionUtil {

    /**
     * Return the selected code
     *
     * @param editor the editor
     * @return null if we aren't in a situation in which we can find code to executed,
     * otherwise the selected code
     */
    public static @Nullable String getSelectedCode(@NotNull Editor editor) {
        // simply returns the selected code
        // the goal of the user is beyond our understanding
        return editor.getSelectionModel().getSelectedText();
    }

    // rip, we need to find what the user want to send to the console
    private static @Nullable Pair<PsiElement, PsiFile> findSelectedElement(@NotNull Editor editor) {
        PsiFile psiFile = OCamlPsiUtils.getPsiFile(editor);
        if (psiFile == null) return null;
        PsiElement elementAt = psiFile.findElementAt(editor.getCaretModel().getOffset());
        if (elementAt == null) { // did we reach the end of the file?
            // maybe we can go back a bit
            elementAt = psiFile.findElementAt(editor.getCaretModel().getOffset()-1);
            if (elementAt == null) return null;
        }
        PsiElement s = findStatementBefore(elementAt);
        if (s == null) {
            s = findStatementAfter(elementAt);
            if (s == null) return null;
        }
        return new Pair<>(new PsiLetWithAnd(s), psiFile);
    }

    public static @Nullable ArrayList<PsiElement> autoSelectStatements(@NotNull Editor editor) {
        Pair<PsiElement, PsiFile> statement = findSelectedElement(editor);
        if (statement == null) return null;
        PsiElement current = statement.first;
        ArrayList<PsiElement> candidates = new ArrayList<>();

        // go up until we found the file
        while (current != statement.second) {
            // add every candidate
            candidates.add(0, current);
            current = current.getParent();
        }

        return candidates;
    }

    private static @Nullable PsiElement findStatementBefore(@Nullable PsiElement elementAt) {
        if (elementAt == null) return null;
        if (elementAt instanceof PsiStructuredElement) return elementAt;
        PsiElement prevSibling = elementAt.getPrevSibling();
        if (prevSibling == null) prevSibling = elementAt.getParent();
        return findStatementBefore(prevSibling);
    }

    private static @Nullable PsiElement findStatementAfter(@Nullable PsiElement elementAt) {
        if (elementAt == null) return null;
        if (elementAt instanceof PsiStructuredElement) return elementAt;
        PsiElement nextSibling = elementAt.getNextSibling();
        if (nextSibling == null) nextSibling = elementAt.getParent();
        return findStatementAfter(nextSibling);
    }

}
