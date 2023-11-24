package com.or.ide.files;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.lang.Language;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiQualifiedNamedElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.ocaml.ide.files.FileHelper;
import com.or.lang.core.ORUtil;
import com.or.lang.core.psi.PsiExternal;
import com.or.lang.core.psi.PsiLet;
import com.or.lang.core.psi.PsiQualifiedPathElement;
import com.or.lang.utils.ModuleHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public abstract class FileBase extends PsiFileBase implements PsiQualifiedPathElement {
    private final @NotNull String m_moduleName;

    FileBase(@NotNull FileViewProvider viewProvider, @NotNull Language language) {
        super(viewProvider, language);
        m_moduleName = ORUtil.fileNameToModuleName(getName());
    }

    public @NotNull String getModuleName() {
        return m_moduleName;
    }

    //region PsiQualifiedName
    @Override
    public String [] getPath() {
        return null;
    }

    @Override
    public @NotNull String getQualifiedName() {
        return getModuleName();
    }
    //endregion

    public boolean isComponent() {
        if (FileHelper.isOCaml(getFileType())) {
            return false;
        }

        return ModuleHelper.isComponent(this);
    }

    public @Nullable PsiElement getComponentNavigationElement() {
        if (isComponent()) {
            List<PsiLet> lets = PsiTreeUtil.getStubChildrenOfTypeAsList(this, PsiLet.class);
            for (PsiLet let : lets) {
                if ("make".equals(let.getName())) {
                    return let;
                }
            }
            List<PsiExternal> externals = PsiTreeUtil.getStubChildrenOfTypeAsList(this, PsiExternal.class);
            for (PsiExternal external : externals) {
                if ("make".equals(external.getName())) {
                    return external;
                }
            }
        }
        return null;
    }

    @SafeVarargs
    public @NotNull final <T extends PsiQualifiedNamedElement> List<T> getQualifiedExpressions(@Nullable String name, @NotNull Class<? extends T>... clazz) {
        List<T> result = new ArrayList<>();

        if (name != null) {
            Collection<T> children = PsiTreeUtil.findChildrenOfAnyType(this, clazz);
            for (T child : children) {
                if (name.equals(child.getQualifiedName())) {
                    result.add(child);
                }
            }
        }

        return result;
    }

    public boolean isInterface() {
        return FileHelper.isInterface(getFileType());
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FileBase fileBase = (FileBase) o;
        return m_moduleName.equals(fileBase.m_moduleName) && isInterface() == fileBase.isInterface();
    }

    @Override
    public int hashCode() {
        return Objects.hash(m_moduleName, isInterface());
    }
}
