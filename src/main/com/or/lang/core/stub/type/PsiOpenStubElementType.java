package com.or.lang.core.stub.type;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.or.ide.search.index.IndexKeys;
import com.or.lang.core.psi.PsiOpen;
import com.or.lang.core.psi.impl.PsiOpenImpl;
import com.or.lang.core.stub.PsiOpenStub;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class PsiOpenStubElementType extends ORStubElementType<PsiOpenStub, PsiOpen> {
    public static final int VERSION = 2;

    public PsiOpenStubElementType() {
        super("C_OPEN");
    }

    @Override
    public @NotNull PsiOpen createPsi(@NotNull PsiOpenStub stub) {
        return new PsiOpenImpl(stub, this);
    }

    @Override
    public @NotNull PsiElement createPsi(@NotNull ASTNode node) {
        return new PsiOpenImpl(node);
    }

    @Override
    public @NotNull PsiOpenStub createStub(@NotNull PsiOpen psi, StubElement parentStub) {
        return new PsiOpenStub(parentStub, this, psi.getPath());
    }

    @Override
    public void serialize(@NotNull PsiOpenStub stub, @NotNull StubOutputStream dataStream) throws IOException {
        dataStream.writeUTFFast(stub.getOpenPath());
    }

    @Override
    public @NotNull PsiOpenStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
        String openPath = dataStream.readUTFFast();
        return new PsiOpenStub(parentStub, this, openPath);
    }

    @Override
    public void indexStub(@NotNull PsiOpenStub stub, @NotNull IndexSink sink) {
        sink.occurrence(IndexKeys.OPENS, stub.getOpenPath());
    }

    @Override
    public @NotNull String getExternalId() {
        return getLanguage().getID() + "." + super.toString();
    }
}
