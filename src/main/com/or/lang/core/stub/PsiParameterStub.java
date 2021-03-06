package com.or.lang.core.stub;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.NamedStubBase;
import com.intellij.psi.stubs.StubElement;
import com.intellij.util.io.StringRef;
import com.or.lang.core.psi.PsiParameter;
import org.jetbrains.annotations.NotNull;

public class PsiParameterStub extends NamedStubBase<PsiParameter> {
    private final String[] myPath;
    private final String myQname;

    public PsiParameterStub(StubElement parent, @NotNull IStubElementType elementType, String name, String[] path, String qName) {
        super(parent, elementType, name);
        myPath = path;
        myQname = qName;
    }

    public PsiParameterStub(StubElement parent, @NotNull IStubElementType elementType, StringRef name, String[] path, String qname) {
        super(parent, elementType, name);
        myPath = path;
        myQname = qname;
    }

    public String[] getPath() {
        return myPath;
    }

    public String getQualifiedName() {
        return myQname;
    }
}
