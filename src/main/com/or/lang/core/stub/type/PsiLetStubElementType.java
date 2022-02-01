package com.or.lang.core.stub.type;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.intellij.util.io.StringRef;
import com.or.ide.search.index.IndexKeys;
import com.or.lang.core.psi.PsiLet;
import com.or.lang.core.psi.impl.PsiLetImpl;
import com.or.lang.core.psi.impl.PsiLowerIdentifier;
import com.or.lang.core.stub.PsiLetStub;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PsiLetStubElementType extends ORStubElementType<PsiLetStub, PsiLet> {
    public static final int VERSION = 14;

    public PsiLetStubElementType() {
        super("C_LET_DECLARATION");
    }

    @NotNull
    public PsiLetImpl createPsi(@NotNull PsiLetStub stub) {
        return new PsiLetImpl(stub, this);
    }

    @Override
    public @NotNull PsiElement createPsi(@NotNull ASTNode node) {
        return new PsiLetImpl(node);
    }

    @NotNull
    public PsiLetStub createStub(@NotNull PsiLet psi, StubElement parentStub) {
        List<String> deconstructedNames = new ArrayList<>();
        if (psi.isDeconstruction()) {
            List<PsiElement> elements = psi.getDeconstructedElements();
            for (PsiElement element : elements) {
                if (element instanceof PsiLowerIdentifier) {
                    deconstructedNames.add(element.getText());
                }
            }
        }
        return new PsiLetStub(parentStub, this, psi.getName(), psi.getPath(), psi.getAlias(), psi.isFunction(), deconstructedNames);
    }

    public void serialize(@NotNull PsiLetStub stub, @NotNull StubOutputStream dataStream) throws IOException {
        dataStream.writeName(stub.getName());
        SerializerUtil.writePath(dataStream, stub.getPath());
        dataStream.writeBoolean(stub.isFunction());

        List<String> deconstructionNames = stub.getDeconstructionNames();
        dataStream.writeByte(deconstructionNames.size());
        if (!deconstructionNames.isEmpty()) {
            for (String name : deconstructionNames) {
                dataStream.writeUTFFast(name);
            }
        }

        String alias = stub.getAlias();
        dataStream.writeBoolean(alias != null);
        if (alias != null) {
            dataStream.writeUTFFast(stub.getAlias());
        }
    }

    @NotNull
    public PsiLetStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
        StringRef name = dataStream.readName();
        String[] path = SerializerUtil.readPath(dataStream);
        boolean isFunction = dataStream.readBoolean();

        List<String> deconstructionNames = new ArrayList<>();
        byte namesCount = dataStream.readByte();
        if (namesCount > 0) {
            for (int i = 0; i < namesCount; i++) {
                deconstructionNames.add(dataStream.readUTFFast());
            }
        }

        String alias = null;
        boolean isAlias = dataStream.readBoolean();
        if (isAlias) {
            alias = dataStream.readUTFFast();
        }

        return new PsiLetStub(parentStub, this, name, path, alias, isFunction, deconstructionNames);
    }

    public void indexStub(@NotNull PsiLetStub stub, @NotNull IndexSink sink) {
        List<String> deconstructionNames = stub.getDeconstructionNames();
        if (deconstructionNames.isEmpty()) {
            // Normal let

            String name = stub.getName();
            if (name != null) {
                sink.occurrence(IndexKeys.LETS, name);
            }

            String fqn = stub.getQualifiedName();
            sink.occurrence(IndexKeys.LETS_FQN, fqn.hashCode());
        } else {
            // Deconstruction

            for (String name : deconstructionNames) {
                sink.occurrence(IndexKeys.LETS, name);
            }

            for (String fqn : stub.getQualifiedNames()) {
                sink.occurrence(IndexKeys.LETS_FQN, fqn.hashCode());
            }
        }
    }

    public @NotNull String getExternalId() {
        return getLanguage().getID() + "." + super.toString();
    }
}
