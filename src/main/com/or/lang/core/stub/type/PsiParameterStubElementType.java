package com.or.lang.core.stub.type;

import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.intellij.util.io.StringRef;
import com.or.ide.search.index.IndexKeys;
import com.or.lang.core.psi.PsiParameter;
import com.or.lang.core.psi.impl.PsiParameterImpl;
import com.or.lang.core.stub.PsiParameterStub;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class PsiParameterStubElementType extends ORStubElementType<PsiParameterStub, PsiParameter> {
    public static final int VERSION = 5;

    public PsiParameterStubElementType(@NotNull String name) {
        super(name);
    }

    @NotNull
    public PsiParameterImpl createPsi(@NotNull PsiParameterStub stub) {
        return new PsiParameterImpl(stub, this);
    }

    @NotNull
    public PsiParameterImpl createPsi(@NotNull ASTNode node) {
        return new PsiParameterImpl(node);
    }

    @NotNull
    public PsiParameterStub createStub(@NotNull PsiParameter psi, StubElement parentStub) {
        return new PsiParameterStub(parentStub, this, psi.getName(), psi.getPath(), psi.getQualifiedName());
    }

    public void serialize(@NotNull PsiParameterStub stub, @NotNull StubOutputStream dataStream) throws IOException {
        dataStream.writeName(stub.getName());
        SerializerUtil.writePath(dataStream, stub.getPath());
        dataStream.writeUTFFast(stub.getQualifiedName());
    }

    @NotNull
    public PsiParameterStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
        StringRef name = dataStream.readName();
        String[] path = SerializerUtil.readPath(dataStream);
        String qname = dataStream.readUTFFast();
        return new PsiParameterStub(parentStub, this, name, path, qname);
    }

    public void indexStub(@NotNull PsiParameterStub stub, @NotNull IndexSink sink) {
        String name = stub.getName();
        if (name != null) {
            sink.occurrence(IndexKeys.PARAMETERS, name);
        }

        String fqn = stub.getQualifiedName();
        if (fqn != null) {
            sink.occurrence(IndexKeys.PARAMETERS_FQN, fqn.hashCode());
        }
    }

    @NotNull
    public String getExternalId() {
        return getLanguage().getID() + "." + super.toString();
    }
}
