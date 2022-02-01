package com.or.lang.core.stub.type;

import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.intellij.util.io.StringRef;
import com.or.ide.search.index.IndexKeys;
import com.or.lang.core.psi.PsiRecordField;
import com.or.lang.core.psi.impl.PsiRecordFieldImpl;
import com.or.lang.core.stub.PsiRecordFieldStub;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class PsiRecordFieldStubElementType extends ORStubElementType<PsiRecordFieldStub, PsiRecordField> {
    public static final int VERSION = 5;

    public PsiRecordFieldStubElementType() {
        super("C_RECORD_FIELD");
    }

    @NotNull
    public PsiRecordFieldImpl createPsi(@NotNull PsiRecordFieldStub stub) {
        return new PsiRecordFieldImpl(stub, this);
    }

    @NotNull
    public PsiRecordFieldImpl createPsi(@NotNull ASTNode node) {
        return new PsiRecordFieldImpl(node);
    }

    @NotNull
    public PsiRecordFieldStub createStub(@NotNull PsiRecordField psi, StubElement parentStub) {
        return new PsiRecordFieldStub(parentStub, this, psi.getName(), psi.getPath());
    }

    public void serialize(@NotNull final PsiRecordFieldStub stub, @NotNull final StubOutputStream dataStream) throws IOException {
        dataStream.writeName(stub.getName());
        SerializerUtil.writePath(dataStream, stub.getPath());
    }

    @NotNull
    public PsiRecordFieldStub deserialize(@NotNull final StubInputStream dataStream, final StubElement parentStub) throws IOException {
        StringRef name = dataStream.readName();
        String[] path = SerializerUtil.readPath(dataStream);
        return new PsiRecordFieldStub(parentStub, this, name, path);
    }

    public void indexStub(@NotNull PsiRecordFieldStub stub, @NotNull IndexSink sink) {
        String name = stub.getName();
        if (name != null) {
            sink.occurrence(IndexKeys.RECORD_FIELDS, name);
        }
    }

    @NotNull
    public String getExternalId() {
        return getLanguage().getID() + "." + super.toString();
    }
}
