package com.or.lang.core.stub.type;

import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.intellij.util.io.StringRef;
import com.or.ide.search.index.IndexKeys;
import com.or.lang.core.psi.PsiType;
import com.or.lang.core.psi.impl.PsiTypeImpl;
import com.or.lang.core.stub.PsiTypeStub;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class PsiTypeStubElementType extends ORStubElementType<PsiTypeStub, PsiType> {
    public static final int VERSION = 11;

    public PsiTypeStubElementType() {
        super("C_TYPE_DECLARATION");
    }

    public @NotNull PsiTypeImpl createPsi(@NotNull PsiTypeStub stub) {
        return new PsiTypeImpl(stub, this);
    }

    public @NotNull PsiTypeImpl createPsi(@NotNull ASTNode node) {
        return new PsiTypeImpl(node);
    }

    public @NotNull PsiTypeStub createStub(@NotNull PsiType psi, StubElement parentStub) {
        return new PsiTypeStub(parentStub, this, psi.getName(), psi.getPath(), psi.isAbstract(), psi.isJsObject(), psi.isRecord());
    }

    public void serialize(@NotNull PsiTypeStub stub, @NotNull StubOutputStream dataStream) throws IOException {
        dataStream.writeName(stub.getName());
        SerializerUtil.writePath(dataStream, stub.getPath());
        dataStream.writeBoolean(stub.isAbstract());
        dataStream.writeBoolean(stub.isJsObject());
        dataStream.writeBoolean(stub.isRecord());
    }

    @NotNull
    public PsiTypeStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
        StringRef name = dataStream.readName();
        String[] path = SerializerUtil.readPath(dataStream);
        boolean isAbstract = dataStream.readBoolean();
        boolean isJsObject = dataStream.readBoolean();
        boolean isRecord = dataStream.readBoolean();
        return new PsiTypeStub(parentStub, this, name, path, isAbstract, isJsObject, isRecord);
    }

    public void indexStub(@NotNull PsiTypeStub stub, @NotNull IndexSink sink) {
        String name = stub.getName();
        if (name != null) {
            sink.occurrence(IndexKeys.TYPES, name);
        }

        String fqn = stub.getQualifiedName();
        sink.occurrence(IndexKeys.TYPES_FQN, fqn.hashCode());
    }

    @NotNull
    public String getExternalId() {
        return getLanguage().getID() + "." + super.toString();
    }
}
