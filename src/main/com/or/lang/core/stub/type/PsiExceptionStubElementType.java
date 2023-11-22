package com.or.lang.core.stub.type;

import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.intellij.util.io.StringRef;
import com.or.ide.search.index.IndexKeys;
import com.or.lang.core.psi.PsiException;
import com.or.lang.core.psi.impl.PsiExceptionImpl;
import com.or.lang.core.stub.PsiExceptionStub;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class PsiExceptionStubElementType extends ORStubElementType<PsiExceptionStub, PsiException> {
    public static final int VERSION = 7;

    public PsiExceptionStubElementType() {
        super("C_EXCEPTION_DECLARATION");
    }

    @NotNull
    public PsiException createPsi(@NotNull PsiExceptionStub stub) {
        return new PsiExceptionImpl(stub, this);
    }

    @NotNull
    public PsiException createPsi(@NotNull ASTNode node) {
        return new PsiExceptionImpl(node);
    }

    @NotNull
    public PsiExceptionStub createStub(@NotNull PsiException psi, StubElement parentStub) {
        String[] path = psi.getPath();
        return new PsiExceptionStub(parentStub, this, psi.getName(), path == null ? EMPTY_PATH : path, psi.getAlias());
    }

    public void serialize(@NotNull PsiExceptionStub stub, @NotNull StubOutputStream dataStream) throws IOException {
        dataStream.writeName(stub.getName());
        SerializerUtil.writePath(dataStream, stub.getPath());
        dataStream.writeName(stub.getAlias());
    }

    @NotNull
    public PsiExceptionStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
        StringRef name = dataStream.readName();
        String[] path = SerializerUtil.readPath(dataStream);
        StringRef alias = dataStream.readName();

        return new PsiExceptionStub(parentStub, this, name, path, alias == null ? null : alias.getString());
    }

    public void indexStub(@NotNull PsiExceptionStub stub, @NotNull IndexSink sink) {
        String name = stub.getName();
        if (name != null) {
            sink.occurrence(IndexKeys.EXCEPTIONS, name);
        }

        String fqn = stub.getQualifiedName();
        sink.occurrence(IndexKeys.EXCEPTIONS_FQN, fqn.hashCode());
    }

    @NotNull
    public String getExternalId() {
        return getLanguage().getID() + "." + super.toString();
    }
}
