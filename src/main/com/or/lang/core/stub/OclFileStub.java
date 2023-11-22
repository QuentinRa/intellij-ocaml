package com.or.lang.core.stub;

import com.intellij.psi.stubs.PsiFileStubImpl;
import com.intellij.psi.tree.IStubFileElementType;
import com.or.ide.files.FileBase;
import com.or.lang.core.stub.type.OclFileStubElementType;
import org.jetbrains.annotations.NotNull;

public class OclFileStub extends PsiFileStubImpl<FileBase> {
    public OclFileStub(FileBase file) {
        super(file);
    }

    @Override
    public @NotNull IStubFileElementType<OclFileStub> getType() {
        return OclFileStubElementType.INSTANCE;
    }
}
