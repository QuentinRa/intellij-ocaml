package com.or.lang.core.stub;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import com.intellij.util.io.StringRef;
import com.or.lang.core.psi.PsiVal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PsiValStub extends PsiQualifiedNameStub<PsiVal> {
    private final boolean myIsFunction;

    public PsiValStub(@Nullable StubElement parent, @NotNull IStubElementType elementType, @Nullable String name, @NotNull String[] path, boolean isFunction) {
        super(parent, elementType, name, path);
        myIsFunction = isFunction;
    }

    public PsiValStub(@Nullable StubElement parent, @NotNull IStubElementType elementType, @Nullable StringRef name, @NotNull String[] path, boolean isFunction) {
        super(parent, elementType, name, path);
        myIsFunction = isFunction;
    }

    public boolean isFunction() {
        return myIsFunction;
    }
}
