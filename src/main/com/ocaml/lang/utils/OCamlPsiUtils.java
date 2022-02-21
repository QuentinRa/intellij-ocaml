package com.ocaml.lang.utils;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.or.lang.OCamlTypes;
import com.or.lang.core.psi.PsiInclude;
import com.or.lang.core.psi.PsiOpen;
import com.or.lang.core.psi.PsiUpperSymbol;
import com.or.lang.core.type.ORTokenElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public final class OCamlPsiUtils {

    /**
     * Return the name of the files that are explicit dependencies of this file, without
     * their extensions (it's up to the caller to find them, and pick either the .ml or the .mli)
     * @param element at first, it must be a file, then it's the element that we are exploring. It's working
     *                if the element isn't a file, but it also means that only the dependencies starting
     *                from this element, will be added in the set.
     * @return a set of file names without extensions, that are the explicit dependencies of this file
     */
    public static @NotNull Set<String> findDependencies(@NotNull PsiElement element) {
        Set<String> res = new HashSet<>();
        for (PsiElement child : element.getChildren()) {
            if (child instanceof PsiWhiteSpace) continue;

            // open ModuleName
            if (child instanceof PsiOpen) {
                res.add(((PsiOpen) child).getPath().toLowerCase());
            }

            if (child instanceof PsiInclude) {
                res.add(((PsiInclude) child).getIncludePath().toLowerCase());
            }

            // child instanceof PsiLocalOpen?

            // ModuleName.function
            if (child instanceof PsiUpperSymbol && OCamlPsiUtils.isNextMeaningfulNextSibling(child, OCamlTypes.DOT)) {
                res.add(child.getText().toLowerCase());
            }
            // next
            res.addAll(findDependencies(child));
        }
        return res;
    }

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
