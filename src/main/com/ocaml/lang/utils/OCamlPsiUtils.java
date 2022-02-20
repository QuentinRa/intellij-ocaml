package com.ocaml.lang.utils;

import com.intellij.ide.util.PsiNavigationSupport;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.or.lang.OCamlTypes;
import com.or.lang.core.psi.PsiInclude;
import com.or.lang.core.psi.PsiOpen;
import com.or.lang.core.psi.PsiUpperSymbol;
import com.or.lang.core.type.ORTokenElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public final class OCamlPsiUtils {

    public static int findIndexOfParameter(@NotNull PsiElement element, String functionName) {
        PsiElement prevSibling = element.getPrevSibling();
        int count = 0;
        while (prevSibling != null) {
            // skip white spaces
            // and comments (fix, added)
            if (prevSibling instanceof PsiWhiteSpace || prevSibling instanceof PsiComment) {
                prevSibling = prevSibling.getPrevSibling();
                continue;
            }
            // done, we found the name of the function
            if (prevSibling.getText().equals(functionName)) return count;
            // pass, this is an argument (I hope so ><)
            count++;
            prevSibling = prevSibling.getPrevSibling();
        }

        return -1; // not found
    }

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

    public static boolean isMeaningfulPrevSibling(@NotNull PsiElement element, ORTokenElementType type) {
        PsiElement prevSibling = skipMeaninglessPreviousSibling(element);
        if (prevSibling == null) return false;
        return prevSibling.getNode().getElementType().equals(type);
    }

    public static boolean isNextMeaningfulNextSibling(@NotNull PsiElement element, Class<? extends PsiElement> aClass) {
        PsiElement nextSibling = skipMeaninglessNextSibling(element);
        if (nextSibling == null) return false;
        return aClass.isInstance(nextSibling);
    }

    public static @Nullable PsiElement getNextMeaningfulSibling(@NotNull PsiElement element, ORTokenElementType type) {
        PsiElement nextSibling = skipMeaninglessNextSibling(element);
        if (nextSibling == null) return null;
        return nextSibling.getNode().getElementType().equals(type) ? nextSibling : null;
    }

    // todo: test
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

    /**
     * @see #openFile(Project, VirtualFile, boolean)
     */
    public static void openFile(Project project, File file, boolean requestFocus) {
        VirtualFile virtualFile = LocalFileSystem.getInstance().findFileByIoFile(file);
        if (virtualFile == null) return;
        openFile(project, virtualFile, requestFocus);
    }

    /**
     * Open a file in the editor, may request focus
     *
     * @param project      the project
     * @param file         the file
     * @param requestFocus true if we are requesting the focus.
     * @see PsiNavigationSupport
     */
    public static void openFile(Project project, VirtualFile file, boolean requestFocus) {
        var navigation = PsiNavigationSupport.getInstance();
        navigation.createNavigatable(project, file, -1).navigate(requestFocus);
    }
}
