package com.or.lang.core.stub;

import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.NamedStubBase;
import com.intellij.psi.stubs.StubElement;
import com.intellij.util.io.StringRef;
import com.or.utils.Joiner;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class PsiQualifiedNameStub<T extends PsiNamedElement> extends NamedStubBase<T> {
    private final String[] myPath;
    private final String myQname;

    PsiQualifiedNameStub(@Nullable StubElement parent, @NotNull IStubElementType elementType, @Nullable String name, @NotNull String[] path) {
        super(parent, elementType, name);
        myPath = path;
        myQname = Joiner.join(".", path) + "." + name;
    }

    PsiQualifiedNameStub(@Nullable StubElement parent, @NotNull IStubElementType elementType, @Nullable StringRef name, @NotNull String[] path) {
        super(parent, elementType, name);
        myPath = path;
        myQname = Joiner.join(".", path) + "." + name;
    }

    public @NotNull String[] getPath() {
        return myPath;
    }

    public @NotNull String getQualifiedName() {
        return myQname;
    }
}
