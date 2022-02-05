package com.or.ide.search.index;

import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StringStubIndexExtension;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.psi.stubs.StubIndexKey;
import com.or.lang.core.psi.PsiVal;
import com.or.lang.core.stub.type.PsiValStubElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class ValIndex extends StringStubIndexExtension<PsiVal> {
    public static @NotNull Collection<PsiVal> getElements(@NotNull String key, @NotNull Project project, @Nullable GlobalSearchScope scope) {
        return StubIndex.getElements(IndexKeys.VALS, key, project, scope, PsiVal.class);
    }

    @Override
    public int getVersion() {
        return super.getVersion() + PsiValStubElementType.VERSION;
    }

    @Override
    public @NotNull StubIndexKey<String, PsiVal> getKey() {
        return IndexKeys.VALS;
    }
}
