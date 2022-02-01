package com.or.lang.core.stub;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import com.or.lang.core.psi.PsiOpen;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PsiOpenStub extends StubBase<PsiOpen> {
    private final String myOpenPath;

    public PsiOpenStub(@Nullable StubElement parent, @NotNull IStubElementType elementType, @Nullable String openPath) {
        super(parent, elementType);
        myOpenPath = openPath;
    }

    public @Nullable String getOpenPath() {
        return myOpenPath;
    }
}
