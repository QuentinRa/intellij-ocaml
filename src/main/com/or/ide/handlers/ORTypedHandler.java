package com.or.ide.handlers;

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.ocaml.ide.files.OCamlFileType;
import com.ocaml.ide.files.OCamlInterfaceFileType;
import org.jetbrains.annotations.NotNull;

public class ORTypedHandler extends TypedHandlerDelegate {
    @NotNull
    @Override
    public Result beforeCharTyped(
            char c,
            @NotNull Project project,
            @NotNull Editor editor,
            @NotNull PsiFile file,
            @NotNull FileType fileType) {
        if (!(fileType instanceof OCamlFileType || fileType instanceof OCamlInterfaceFileType)) {
            return Result.CONTINUE;
        }

        // #62 - don't insert a ) when at the end of a comment
        if (c == ')') {
            Document doc = editor.getDocument();
            PsiDocumentManager.getInstance(project).commitDocument(doc);
            CaretModel caretModel = editor.getCaretModel();

            // * <caret> )
            int offsetBefore = caretModel.getOffset();
            if (offsetBefore < doc.getTextLength()) {
                CharSequence charsSequence = doc.getCharsSequence();
                char c1 = charsSequence.charAt(offsetBefore - 1);
                char c2 = charsSequence.charAt(offsetBefore);
                if (c1 == '*' && c2 == ')') {
                    PsiElement leaf = file.findElementAt(offsetBefore);
                    if (leaf instanceof PsiComment) {
                        caretModel.moveToOffset(offsetBefore + 1);
                        return Result.STOP;
                    }
                }
            }
        }

        return super.beforeCharTyped(c, project, editor, file, fileType);
    }
}
