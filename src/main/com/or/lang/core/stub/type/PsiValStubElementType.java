package com.or.lang.core.stub.type;

import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.intellij.util.io.StringRef;
import com.or.ide.search.index.IndexKeys;
import com.or.lang.core.psi.PsiVal;
import com.or.lang.core.psi.impl.PsiValImpl;
import com.or.lang.core.stub.PsiValStub;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class PsiValStubElementType extends ORStubElementType<PsiValStub, PsiVal> {
    public static final int VERSION = 12;

    public PsiValStubElementType() {
        super("C_VAL_DECLARATION");
    }

    @NotNull
    public PsiValImpl createPsi(@NotNull ASTNode node) {
        return new PsiValImpl(node);
    }

    @NotNull
    public PsiValImpl createPsi(@NotNull PsiValStub stub) {
        return new PsiValImpl(stub, this);
    }

    @NotNull
    public PsiValStub createStub(@NotNull PsiVal psi, StubElement parentStub) {
        return new PsiValStub(parentStub, this, psi.getName(), psi.getPath(), psi.isFunction());
    }

    public void serialize(@NotNull PsiValStub stub, @NotNull StubOutputStream dataStream) throws IOException {
        dataStream.writeName(stub.getName());
        SerializerUtil.writePath(dataStream, stub.getPath());
        dataStream.writeBoolean(stub.isFunction());
    }

    @NotNull
    public PsiValStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
        StringRef name = dataStream.readName();
        String[] path = SerializerUtil.readPath(dataStream);
        boolean isFunction = dataStream.readBoolean();

        return new PsiValStub(parentStub, this, name, path, isFunction);
    }

    public void indexStub(@NotNull PsiValStub stub, @NotNull IndexSink sink) {
        String name = stub.getName();
        if (name != null) {
            sink.occurrence(IndexKeys.VALS, name);
        }

        String fqn = stub.getQualifiedName();
        sink.occurrence(IndexKeys.VALS_FQN, fqn.hashCode());
    }

    @NotNull
    public String getExternalId() {
        return getLanguage().getID() + "." + super.toString();
    }
}
