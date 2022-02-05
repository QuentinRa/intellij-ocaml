package com.or.ide.search.index;

import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StringStubIndexExtension;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.psi.stubs.StubIndexKey;
import com.or.lang.core.psi.PsiVariantDeclaration;
import com.or.lang.core.stub.type.PsiVariantStubElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class VariantIndex extends StringStubIndexExtension<PsiVariantDeclaration> {
    public static @NotNull Collection<PsiVariantDeclaration> getElements(@NotNull String key, @NotNull Project project, @Nullable GlobalSearchScope scope) {
        return StubIndex.getElements(IndexKeys.VARIANTS, key, project, scope, PsiVariantDeclaration.class);
    }

    @Override
    public int getVersion() {
        return super.getVersion() + PsiVariantStubElementType.VERSION;
    }

    @Override
    public @NotNull StubIndexKey<String, PsiVariantDeclaration> getKey() {
        return IndexKeys.VARIANTS;
    }
}
