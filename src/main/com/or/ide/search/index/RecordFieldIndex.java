package com.or.ide.search.index;

import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StringStubIndexExtension;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.psi.stubs.StubIndexKey;
import com.or.lang.core.psi.PsiRecordField;
import com.or.lang.core.stub.type.PsiRecordFieldStubElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class RecordFieldIndex extends StringStubIndexExtension<PsiRecordField> {
    public static @NotNull Collection<PsiRecordField> getElements(@NotNull String key, @NotNull Project project, @Nullable GlobalSearchScope scope) {
        return StubIndex.getElements(IndexKeys.RECORD_FIELDS, key, project, scope, PsiRecordField.class);
    }

    @Override
    public int getVersion() {
        return super.getVersion() + PsiRecordFieldStubElementType.VERSION;
    }

    @Override
    public @NotNull StubIndexKey<String, PsiRecordField> getKey() {
        return IndexKeys.RECORD_FIELDS;
    }
}
