package com.ocaml.lang.utils;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.or.lang.core.type.ORTokenElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class OCamlPsiUtils {

    public static boolean isNextMeaningfulNextSibling(@NotNull PsiElement element, ORTokenElementType type) {
        PsiElement nextSibling = skipMeaninglessNextSibling(element);
        if (nextSibling == null) return false;
        return nextSibling.getNode().getElementType().equals(type);
    }

    public static @Nullable PsiElement getNextMeaningfulSibling(@NotNull PsiElement element, ORTokenElementType type) {
        PsiElement nextSibling = skipMeaninglessNextSibling(element);
        if (nextSibling == null) return null;
        return nextSibling.getNode().getElementType().equals(type) ? nextSibling : null;
    }

    public static @Nullable PsiElement getPreviousMeaningfulSibling(@NotNull PsiElement element, ORTokenElementType type) {
        PsiElement prevSibling = skipMeaninglessPreviousSibling(element);
        if (prevSibling == null) return null;
        return prevSibling.getNode().getElementType().equals(type) ? prevSibling : null;
    }

    public static @Nullable PsiElement skipMeaninglessNextSibling(@NotNull PsiElement element) {
        PsiElement nextSibling = element.getNextSibling();
        while (nextSibling != null) {
            // skip white spaces and comments
            if (nextSibling instanceof PsiWhiteSpace || nextSibling instanceof PsiComment) {
                nextSibling = nextSibling.getNextSibling();
                continue;
            }
            break;
        }
        return nextSibling;
    }

    public static @Nullable PsiElement skipMeaninglessPreviousSibling(@NotNull PsiElement element) {
        PsiElement prevSibling = element.getPrevSibling();
        while (prevSibling != null) {
            // skip white spaces and comments
            if (prevSibling instanceof PsiWhiteSpace || prevSibling instanceof PsiComment) {
                prevSibling = prevSibling.getPrevSibling();
                continue;
            }
            break;
        }
        return prevSibling;
    }

    public static @Nullable PsiFile getPsiFile(@NotNull Editor editor) {
        Project project = editor.getProject();
        if (project == null) return null;
        // find psiFile
        Document document = editor.getDocument();
        return PsiDocumentManager.getInstance(project).getPsiFile(document);
    }
}
