package com.or.ide.search.index;

import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StringStubIndexExtension;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.psi.stubs.StubIndexKey;
import com.or.lang.core.psi.PsiException;
import com.or.lang.core.stub.type.PsiExceptionStubElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class ExceptionIndex extends StringStubIndexExtension<PsiException> {
    public static @NotNull Collection<PsiException> getElements(@NotNull String key, @NotNull Project project, @Nullable GlobalSearchScope scope) {
        return StubIndex.getElements(IndexKeys.EXCEPTIONS, key, project, scope, PsiException.class);
    }

    @Override
    public int getVersion() {
        return super.getVersion() + PsiExceptionStubElementType.VERSION;
    }

    @Override
    public @NotNull StubIndexKey<String, PsiException> getKey() {
        return IndexKeys.EXCEPTIONS;
    }
}
