package com.or.ide.search.index;

import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.IntStubIndexExtension;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.psi.stubs.StubIndexKey;
import com.or.lang.core.psi.PsiKlass;
import com.or.lang.core.stub.type.PsiKlassStubElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class KlassFqnIndex extends IntStubIndexExtension<PsiKlass> {
    public static @NotNull Collection<PsiKlass> getElements(int key, @NotNull Project project, @Nullable GlobalSearchScope scope) {
        return StubIndex.getElements(IndexKeys.CLASSES_FQN, key, project, scope, PsiKlass.class);
    }

    @Override
    public int getVersion() {
        return super.getVersion() + PsiKlassStubElementType.VERSION;
    }

    @Override
    public @NotNull StubIndexKey<Integer, PsiKlass> getKey() {
        return IndexKeys.CLASSES_FQN;
    }
}
