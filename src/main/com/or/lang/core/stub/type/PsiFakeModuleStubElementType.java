package com.or.lang.core.stub.type;

import com.intellij.lang.ASTNode;
import com.or.lang.core.psi.impl.PsiFakeModule;
import com.or.lang.core.stub.PsiModuleStub;
import com.or.lang.core.type.ORCompositeType;
import org.jetbrains.annotations.NotNull;

public class PsiFakeModuleStubElementType extends PsiModuleStubElementType implements ORCompositeType {
    public PsiFakeModuleStubElementType() {
        super("C_FAKE_MODULE");
    }

    @NotNull
    public PsiFakeModule createPsi(@NotNull final ASTNode node) {
        return new PsiFakeModule(node);
    }

    @NotNull
    public PsiFakeModule createPsi(@NotNull final PsiModuleStub stub) {
        return new PsiFakeModule(stub, this);
    }
}
