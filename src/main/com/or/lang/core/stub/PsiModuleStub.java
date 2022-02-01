package com.or.lang.core.stub;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.NamedStubBase;
import com.intellij.psi.stubs.StubElement;
import com.intellij.util.io.StringRef;
import com.or.lang.core.psi.PsiModule;
import com.or.utils.Joiner;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PsiModuleStub extends NamedStubBase<PsiModule> {
    private final String[] myPath;
    private final String[] myQualifiedNameAsPath;
    private final String myQname;
    private final String myAlias;
    private final boolean myIsComponent;
    private final boolean myIsInterface;
    private final boolean myIsTopLevel;
    private final boolean myIsFunctorCall;

    public PsiModuleStub(StubElement parent, @NotNull IStubElementType elementType, @Nullable String name,
                         String @Nullable [] path, @Nullable String[] qNamePath, @Nullable String namespace, String alias, boolean isComponent,
                         boolean isInterface, boolean isTopLevel, boolean isFunctorCall) {
        super(parent, elementType, name);
        myPath = path;
        myQualifiedNameAsPath = qNamePath;
        myQname = namespace == null ? path != null && path.length > 0 ? Joiner.join(".", path) + "." + name : "" + name : namespace;
        myAlias = alias;
        myIsComponent = isComponent;
        myIsInterface = isInterface;
        myIsTopLevel = isTopLevel;
        myIsFunctorCall = isFunctorCall;
    }

    public PsiModuleStub(StubElement parent, @NotNull IStubElementType elementType, @Nullable StringRef name,
                         String @Nullable [] path, @Nullable String[] qNamePath, @Nullable String namespace, String alias, boolean isComponent,
                         boolean isInterface, boolean isTopLevel, boolean isFunctorCall) {
        super(parent, elementType, name);
        myPath = path;
        myQualifiedNameAsPath = qNamePath;
        myQname = namespace == null ? path != null && path.length > 0 ? Joiner.join(".", path) + "." + name : "" + name : namespace;
        myAlias = alias;
        myIsComponent = isComponent;
        myIsInterface = isInterface;
        myIsTopLevel = isTopLevel;
        myIsFunctorCall = isFunctorCall;
    }

    public String @Nullable [] getPath() {
        return myPath;
    }

    public @NotNull String getQualifiedName() {
        return myQname;
    }

    public String getAlias() {
        return myAlias;
    }

    public boolean isComponent() {
        return myIsComponent;
    }

    public boolean isInterface() {
        return myIsInterface;
    }

    public boolean isTopLevel() {
        return myIsTopLevel;
    }

    public boolean isFunctorCall() {
        return myIsFunctorCall;
    }

    public @Nullable String[] getQualifiedNameAsPath() {
        return myQualifiedNameAsPath;
    }
}
