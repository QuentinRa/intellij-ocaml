package com.or.ide.search.index;

import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StringStubIndexExtension;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.psi.stubs.StubIndexKey;
import com.or.lang.core.psi.PsiExternal;
import com.or.lang.core.stub.type.PsiExternalStubElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class ExternalIndex extends StringStubIndexExtension<PsiExternal> {
    public static @NotNull Collection<PsiExternal> getElements(@NotNull String key, @NotNull Project project, @Nullable GlobalSearchScope scope) {
        return StubIndex.getElements(IndexKeys.EXTERNALS, key, project, scope, PsiExternal.class);
    }

    @Override
    public int getVersion() {
        return super.getVersion() + PsiExternalStubElementType.VERSION;
    }

    @Override
    public @NotNull StubIndexKey<String, PsiExternal> getKey() {
        return IndexKeys.EXTERNALS;
    }
}
