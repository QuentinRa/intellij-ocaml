package com.or.lang.core.stub;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import com.intellij.util.io.StringRef;
import com.or.lang.core.psi.PsiException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PsiExceptionStub extends PsiQualifiedNameStub<PsiException> {
    private final String myAlias;

    public PsiExceptionStub(@Nullable StubElement parent, @NotNull IStubElementType elementType, @Nullable String name, @NotNull String[] path, @Nullable String alias) {
        super(parent, elementType, name, path);
        myAlias = alias;
    }

    public PsiExceptionStub(@Nullable StubElement parent, @NotNull IStubElementType elementType, @Nullable StringRef name, @NotNull String[] path, @Nullable String alias) {
        super(parent, elementType, name, path);
        myAlias = alias;
    }

    public @Nullable String getAlias() {
        return myAlias;
    }
}
