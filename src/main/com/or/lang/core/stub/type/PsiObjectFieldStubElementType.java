package com.or.lang.core.stub.type;

import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.intellij.util.io.StringRef;
import com.or.ide.search.index.IndexKeys;
import com.or.lang.core.psi.impl.PsiObjectField;
import com.or.lang.core.stub.PsiObjectFieldStub;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class PsiObjectFieldStubElementType extends ORStubElementType<PsiObjectFieldStub, PsiObjectField> {
    public static final int VERSION = 2;

    public PsiObjectFieldStubElementType() {
        super("C_OBJECT_FIELD");
    }

    @NotNull
    public PsiObjectField createPsi(@NotNull PsiObjectFieldStub stub) {
        return new PsiObjectField(stub, this);
    }

    @NotNull
    public PsiObjectField createPsi(@NotNull ASTNode node) {
        return new PsiObjectField(node);
    }

    @NotNull
    public PsiObjectFieldStub createStub(@NotNull PsiObjectField psi, StubElement parentStub) {
        return new PsiObjectFieldStub(parentStub, this, psi.getName(), psi.getPath());
    }

    public void serialize(@NotNull final PsiObjectFieldStub stub, @NotNull final StubOutputStream dataStream) throws IOException {
        dataStream.writeName(stub.getName());
        SerializerUtil.writePath(dataStream, stub.getPath());
    }

    @NotNull
    public PsiObjectFieldStub deserialize(@NotNull final StubInputStream dataStream, final StubElement parentStub) throws IOException {
        StringRef name = dataStream.readName();
        String[] path = SerializerUtil.readPath(dataStream);
        return new PsiObjectFieldStub(parentStub, this, name, path);
    }

    public void indexStub(@NotNull PsiObjectFieldStub stub, @NotNull IndexSink sink) {
        String name = stub.getName();
        if (name != null) {
            sink.occurrence(IndexKeys.OBJECT_FIELDS, name);
        }
    }

    @NotNull
    public String getExternalId() {
        return getLanguage().getID() + "." + super.toString();
    }
}
