package com.or.ide.search.index;

import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.IntStubIndexExtension;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.psi.stubs.StubIndexKey;
import com.or.lang.core.psi.PsiVal;
import com.or.lang.core.stub.type.PsiValStubElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class ValFqnIndex extends IntStubIndexExtension<PsiVal> {
    public static @NotNull Collection<PsiVal> getElements(int key, @NotNull Project project, @Nullable GlobalSearchScope scope) {
        return StubIndex.getElements(IndexKeys.VALS_FQN, key, project, scope, PsiVal.class);
    }

    @Override
    public int getVersion() {
        return super.getVersion() + PsiValStubElementType.VERSION;
    }

    @Override
    public @NotNull StubIndexKey<Integer, PsiVal> getKey() {
        return IndexKeys.VALS_FQN;
    }
}
