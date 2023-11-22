package com.or.ide.search.index;

import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.IntStubIndexExtension;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.psi.stubs.StubIndexKey;
import com.or.lang.core.psi.PsiModule;
import com.or.lang.core.stub.type.PsiModuleStubElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class ModuleFqnIndex extends IntStubIndexExtension<PsiModule> {
    public static @NotNull Collection<PsiModule> getElements(@NotNull String qname, @NotNull Project project, @Nullable GlobalSearchScope scope) {
        return StubIndex.getElements(IndexKeys.MODULES_FQN, qname.hashCode(), project, scope, PsiModule.class);
    }

    @Override
    public int getVersion() {
        return super.getVersion() + PsiModuleStubElementType.VERSION;
    }

    @Override
    public @NotNull StubIndexKey<Integer, PsiModule> getKey() {
        return IndexKeys.MODULES_FQN;
    }
}
