package com.or.lang.core.stub.type;

import com.intellij.psi.PsiFile;
import com.intellij.psi.StubBuilder;
import com.intellij.psi.stubs.*;
import com.intellij.psi.tree.IStubFileElementType;
import com.ocaml.OCamlLanguage;
import com.or.ide.files.OclFile;
import com.or.ide.files.OclInterfaceFile;
import com.or.lang.core.stub.OclFileStub;
import org.jetbrains.annotations.NotNull;

public class OclFileStubElementType extends IStubFileElementType<OclFileStub> {
    public static final IStubFileElementType<OclFileStub> INSTANCE = new OclFileStubElementType();
    private static final int VERSION = 9;

    private OclFileStubElementType() {
        super("OCAML_FILE", OCamlLanguage.INSTANCE);
    }

    @Override
    public @NotNull StubBuilder getBuilder() {
        return new DefaultStubBuilder() {
            @Override
            protected @NotNull PsiFileStub<? extends PsiFile> createStubForFile(@NotNull PsiFile file) {
                if (file instanceof OclFile) {
                    return new OclFileStub((OclFile) file);
                } else if (file instanceof OclInterfaceFile) {
                    return new OclFileStub((OclInterfaceFile) file);
                }
                return new PsiFileStubImpl<>(file);
            }
        };
    }

    @Override
    public int getStubVersion() {
        return VERSION;
    }

    @Override
    public void serialize(@NotNull OclFileStub stub, @NotNull StubOutputStream dataStream) {
    }

    @Override
    public @NotNull OclFileStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) {
        return new OclFileStub(null);
    }

    @Override
    public @NotNull String getExternalId() {
        return "ocaml.FILE";
    }
}
