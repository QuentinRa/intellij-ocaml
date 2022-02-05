package com.or.ide.search.index;

import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StringStubIndexExtension;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.psi.stubs.StubIndexKey;
import com.or.lang.core.psi.PsiParameter;
import com.or.lang.core.stub.type.PsiParameterStubElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class ParameterIndex extends StringStubIndexExtension<PsiParameter> {
    public static @NotNull Collection<PsiParameter> getElements(@NotNull String key, @NotNull Project project, @Nullable GlobalSearchScope scope) {
        return StubIndex.getElements(IndexKeys.PARAMETERS, key, project, scope, PsiParameter.class);
    }

    @Override
    public int getVersion() {
        return super.getVersion() + PsiParameterStubElementType.VERSION;
    }

    @Override
    public @NotNull StubIndexKey<String, PsiParameter> getKey() {
        return IndexKeys.PARAMETERS;
    }
}
