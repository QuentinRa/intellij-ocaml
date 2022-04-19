package com.or.lang.core;

import com.intellij.lang.Language;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFileFactory;
import com.ocaml.OCamlLanguage;
import com.or.ide.files.FileBase;
import com.or.lang.core.psi.PsiInnerModule;
import com.or.lang.core.psi.PsiLet;
import com.or.lang.core.psi.PsiLetBinding;
import com.or.lang.core.psi.PsiLowerSymbol;
import com.or.lang.core.psi.impl.PsiLowerIdentifier;
import com.or.lang.core.psi.impl.PsiUpperIdentifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ORCodeFactory {
    private ORCodeFactory() {
    }

    @Nullable
    public static PsiUpperIdentifier createModuleName(@NotNull Project project, @NotNull String name) {
        FileBase file = createFileFromText(project, OCamlLanguage.INSTANCE, "module " + name + " = {};");
        PsiInnerModule module = ORUtil.findImmediateFirstChildOfClass(file, PsiInnerModule.class);
        return ORUtil.findImmediateFirstChildOfClass(module, PsiUpperIdentifier.class);
    }

    @Nullable
    public static PsiElement createLetName(@NotNull Project project, @NotNull String name) {
        FileBase file = createFileFromText(project, OCamlLanguage.INSTANCE, "let " + name + " = 1;");
        PsiLet let = ORUtil.findImmediateFirstChildOfClass(file, PsiLet.class);
        return let == null ? null : ORUtil.nextSibling(let.getFirstChild());
    }

    @Nullable
    public static PsiElement createLowerSymbol(@NotNull Project project, @NotNull String name) {
        FileBase file = createFileFromText(project, OCamlLanguage.INSTANCE, "let _ = " + name);
        return extractLetBinding(file);
    }

    private static PsiElement extractLetBinding(FileBase file) {
        PsiLet let = ORUtil.findImmediateFirstChildOfClass(file, PsiLet.class);
        PsiLetBinding letBinding = ORUtil.findImmediateFirstChildOfClass(let, PsiLetBinding.class);
        if (letBinding != null) {
            PsiElement[] children = letBinding.getChildren();
            if (children.length > 0)
                return children[0];
        }
        return null;
    }

    @NotNull
    public static FileBase createFileFromText(
            @NotNull Project project, @NotNull Language language, @NotNull String text) {
        return (FileBase)
                PsiFileFactory.getInstance(project).createFileFromText("Dummy", language, text);
    }
}
