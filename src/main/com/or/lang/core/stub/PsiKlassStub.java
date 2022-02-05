package com.or.lang.core.stub;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import com.intellij.util.io.StringRef;
import com.or.lang.core.psi.PsiKlass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PsiKlassStub extends PsiQualifiedNameStub<PsiKlass> {
    public PsiKlassStub(@Nullable StubElement parent, @NotNull IStubElementType elementType, @Nullable String name, @NotNull String[] path) {
        super(parent, elementType, name, path);
    }

    public PsiKlassStub(@Nullable StubElement parent, @NotNull IStubElementType elementType, @Nullable StringRef name, @NotNull String[] path) {
        super(parent, elementType, name, path);
    }
}
