package com.or.lang.core.stub.type;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.intellij.util.io.StringRef;
import com.or.ide.files.FileBase;
import com.or.ide.search.index.IndexKeys;
import com.or.lang.core.psi.PsiInclude;
import com.or.lang.core.psi.impl.PsiIncludeImpl;
import com.or.lang.core.stub.PsiIncludeStub;
import com.or.utils.Joiner;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class PsiIncludeStubElementType extends ORStubElementType<PsiIncludeStub, PsiInclude> {
    public static final int VERSION = 3;

    public PsiIncludeStubElementType() {
        super("C_INCLUDE");
    }

    @Override
    public @NotNull PsiInclude createPsi(@NotNull PsiIncludeStub stub) {
        return new PsiIncludeImpl(stub, this);
    }

    @Override
    public @NotNull PsiElement createPsi(@NotNull ASTNode node) {
        return new PsiIncludeImpl(node);
    }

    @Override
    public @NotNull PsiIncludeStub createStub(@NotNull PsiInclude psi, StubElement parentStub) {
        return new PsiIncludeStub(parentStub, this, ((FileBase) psi.getContainingFile()).getModuleName(), psi.getIncludePath(), psi.getQualifiedPath(), psi.getResolvedPath());
    }

    @Override
    public void serialize(@NotNull PsiIncludeStub stub, @NotNull StubOutputStream dataStream) throws IOException {
        dataStream.writeName(stub.getFileModule());
        dataStream.writeUTFFast(stub.getIncludePath());
        SerializerUtil.writePath(dataStream, stub.getQualifiedPath());
        SerializerUtil.writePath(dataStream, stub.getResolvedPath());
    }

    @Override
    public @NotNull PsiIncludeStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
        StringRef fileModule = dataStream.readName();
        String includePath = dataStream.readUTFFast();
        String[] qualifiedPath = SerializerUtil.readPath(dataStream);
        String[] resolvedPath = SerializerUtil.readPath(dataStream);
        return new PsiIncludeStub(parentStub, this, fileModule, includePath, qualifiedPath, resolvedPath);
    }

    @Override
    public void indexStub(@NotNull PsiIncludeStub stub, @NotNull IndexSink sink) {
        sink.occurrence(IndexKeys.INCLUDES, Joiner.join(".", stub.getResolvedPath()));
    }

    @Override
    public @NotNull String getExternalId() {
        return getLanguage().getID() + "." + super.toString();
    }
}
