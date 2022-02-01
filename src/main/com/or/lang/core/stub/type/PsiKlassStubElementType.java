package com.or.lang.core.stub.type;

import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.intellij.util.io.StringRef;
import com.or.ide.search.index.IndexKeys;
import com.or.lang.core.psi.PsiKlass;
import com.or.lang.core.psi.impl.PsiKlassImpl;
import com.or.lang.core.stub.PsiKlassStub;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class PsiKlassStubElementType extends ORStubElementType<PsiKlassStub, PsiKlass> {
    public static final int VERSION = 2;

    public PsiKlassStubElementType() {
        super("C_CLASS_DECLARATION");
    }

    @NotNull
    public PsiKlassImpl createPsi(@NotNull PsiKlassStub stub) {
        return new PsiKlassImpl(stub, this);
    }

    @NotNull
    public PsiKlassImpl createPsi(@NotNull ASTNode node) {
        return new PsiKlassImpl(node);
    }

    @NotNull
    public PsiKlassStub createStub(@NotNull PsiKlass psi, StubElement parentStub) {
        String[] path = psi.getPath();
        return new PsiKlassStub(parentStub, this, psi.getName(), path == null ? EMPTY_PATH : path);
    }

    public void serialize(@NotNull PsiKlassStub stub, @NotNull StubOutputStream dataStream) throws IOException {
        dataStream.writeName(stub.getName());
        SerializerUtil.writePath(dataStream, stub.getPath());
    }

    @NotNull
    public PsiKlassStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
        StringRef name = dataStream.readName();
        String[] path = SerializerUtil.readPath(dataStream);

        return new PsiKlassStub(parentStub, this, name, path);
    }

    public void indexStub(@NotNull PsiKlassStub stub, @NotNull IndexSink sink) {
        String fqn = stub.getQualifiedName();
        sink.occurrence(IndexKeys.CLASSES_FQN, fqn.hashCode());
    }

    @NotNull
    public String getExternalId() {
        return getLanguage().getID() + "." + super.toString();
    }
}
