package com.or.lang.core.stub.type;

import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.intellij.util.io.StringRef;
import com.or.ide.search.index.IndexKeys;
import com.or.lang.core.psi.PsiExternal;
import com.or.lang.core.psi.impl.PsiExternalImpl;
import com.or.lang.core.stub.PsiExternalStub;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class PsiExternalStubElementType extends ORStubElementType<PsiExternalStub, PsiExternal> {
    public static final int VERSION = 9;

    public PsiExternalStubElementType() {
        super("C_EXTERNAL_DECLARATION");
    }

    @NotNull
    public PsiExternalImpl createPsi(@NotNull PsiExternalStub stub) {
        return new PsiExternalImpl(stub, this);
    }

    @NotNull
    public PsiExternalImpl createPsi(@NotNull ASTNode node) {
        return new PsiExternalImpl(node);
    }

    @NotNull
    public PsiExternalStub createStub(@NotNull PsiExternal psi, StubElement parentStub) {
        String[] path = psi.getPath();
        return new PsiExternalStub(parentStub, this, psi.getName(), path == null ? EMPTY_PATH : path, psi.isFunction());
    }

    public void serialize(@NotNull PsiExternalStub stub, @NotNull StubOutputStream dataStream) throws IOException {
        dataStream.writeName(stub.getName());
        SerializerUtil.writePath(dataStream, stub.getPath());
        dataStream.writeBoolean(stub.isFunction());
    }

    @NotNull
    public PsiExternalStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
        StringRef name = dataStream.readName();
        String[] path = SerializerUtil.readPath(dataStream);
        boolean isFunction = dataStream.readBoolean();

        return new PsiExternalStub(parentStub, this, name, path, isFunction);
    }

    public void indexStub(@NotNull PsiExternalStub stub, @NotNull IndexSink sink) {
        String name = stub.getName();
        if (name != null) {
            sink.occurrence(IndexKeys.EXTERNALS, name);
        }
    }

    @NotNull
    public String getExternalId() {
        return getLanguage().getID() + "." + super.toString();
    }
}
