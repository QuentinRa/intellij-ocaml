package com.or.ide.search.index;

import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StringStubIndexExtension;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.psi.stubs.StubIndexKey;
import com.or.lang.core.psi.PsiType;
import com.or.lang.core.stub.type.PsiTypeStubElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class TypeIndex extends StringStubIndexExtension<PsiType> {
    public static @NotNull Collection<PsiType> getElements(@NotNull String key, @NotNull Project project, @Nullable GlobalSearchScope scope) {
        return StubIndex.getElements(IndexKeys.TYPES, key, project, scope, PsiType.class);
    }

    @Override
    public int getVersion() {
        return super.getVersion() + PsiTypeStubElementType.VERSION;
    }

    @Override
    public @NotNull StubIndexKey<String, PsiType> getKey() {
        return IndexKeys.TYPES;
    }
}
