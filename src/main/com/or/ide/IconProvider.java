package com.or.ide;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.ocaml.icons.OCamlIcons;
import com.ocaml.ide.files.FileHelper;
import com.or.ide.files.FileBase;
import com.or.ide.files.OclFile;
import com.or.ide.files.OclInterfaceFile;
import com.or.lang.core.psi.*;
import com.or.lang.core.psi.impl.PsiFakeModule;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class IconProvider extends com.intellij.ide.IconProvider {
    public static @NotNull Icon getFileModuleIcon(@NotNull FileBase element) {
        return getFileModuleIcon(FileHelper.isInterface(element.getFileType()));
    }

    public static @NotNull Icon getFileModuleIcon(boolean isInterface) {
        return isInterface ? OCamlIcons.Nodes.OCL_FILE_MODULE_INTERFACE : OCamlIcons.Nodes.OCL_FILE_MODULE;
    }

    @Nullable
    @Override
    public Icon getIcon(@NotNull PsiElement psiElement, int flags) {
        PsiElement element =
                psiElement instanceof PsiFakeModule ? psiElement.getContainingFile() : psiElement;
        if (element instanceof PsiFile) {
            if (element instanceof OclFile) {
                return OCamlIcons.FileTypes.OCAML_SOURCE;
            }
            if (element instanceof OclInterfaceFile) {
                return OCamlIcons.FileTypes.OCAML_INTERFACE;
            }
        } else if (element instanceof PsiException) {
            return OCamlIcons.Nodes.EXCEPTION;
        } else if (element instanceof PsiInnerModule) {
            return OCamlIcons.Nodes.INNER_MODULE;
        } else if (element instanceof PsiFunctor) {
            return OCamlIcons.Nodes.FUNCTOR;
        } else if (element instanceof PsiType) {
            return OCamlIcons.Nodes.TYPE;
        } else if (element instanceof PsiVariantDeclaration) {
            return OCamlIcons.Nodes.VARIANT;
        } else if (element instanceof PsiLet) {
            PsiLet let = (PsiLet) element;
            return let.isRecord() ? OCamlIcons.Nodes.OBJECT : (let.isFunction() ? OCamlIcons.Nodes.FUNCTION : OCamlIcons.Nodes.LET);
        } else if (element instanceof PsiExternal) {
            return OCamlIcons.Nodes.EXTERNAL;
        } else if (element instanceof PsiVal) {
            return OCamlIcons.Nodes.VAL;
        }
        return null;
    }
}
