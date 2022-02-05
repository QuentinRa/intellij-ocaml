package com.or.ide.search.index;

import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StringStubIndexExtension;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.psi.stubs.StubIndexKey;
import com.or.lang.core.psi.PsiLet;
import com.or.lang.core.stub.type.PsiLetStubElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class LetIndex extends StringStubIndexExtension<PsiLet> {
    public static @NotNull Collection<PsiLet> getElements(@NotNull String key, @NotNull Project project, @Nullable GlobalSearchScope scope) {
        return StubIndex.getElements(IndexKeys.LETS, key, project, scope, PsiLet.class);
    }

    @Override
    public int getVersion() {
        return super.getVersion() + PsiLetStubElementType.VERSION;
    }

    @Override
    public @NotNull StubIndexKey<String, PsiLet> getKey() {
        return IndexKeys.LETS;
    }
}
