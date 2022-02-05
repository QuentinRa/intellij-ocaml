package com.or.ide.search.index;

import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StringStubIndexExtension;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.psi.stubs.StubIndexKey;
import com.or.lang.core.psi.impl.PsiObjectField;
import com.or.lang.core.stub.type.PsiObjectFieldStubElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class ObjectFieldIndex extends StringStubIndexExtension<PsiObjectField> {
    public static @NotNull Collection<PsiObjectField> getElements(@NotNull String key, @NotNull Project project, @Nullable GlobalSearchScope scope) {
        return StubIndex.getElements(IndexKeys.OBJECT_FIELDS, key, project, scope, PsiObjectField.class);
    }

    @Override
    public int getVersion() {
        return super.getVersion() + PsiObjectFieldStubElementType.VERSION;
    }

    @Override
    public @NotNull StubIndexKey<String, PsiObjectField> getKey() {
        return IndexKeys.OBJECT_FIELDS;
    }
}
