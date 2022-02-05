package com.or.lang.core.stub.type;

import com.intellij.lang.ASTNode;
import com.or.lang.core.psi.PsiInnerModule;
import com.or.lang.core.psi.impl.PsiInnerModuleImpl;
import com.or.lang.core.stub.PsiModuleStub;
import com.or.lang.core.type.ORCompositeType;
import org.jetbrains.annotations.NotNull;

public class PsiInnerModuleStubElementType extends PsiModuleStubElementType implements ORCompositeType {
    public PsiInnerModuleStubElementType() {
        super("C_MODULE_DECLARATION");
    }

    @NotNull
    public PsiInnerModule createPsi(@NotNull ASTNode node) {
        return new PsiInnerModuleImpl(node);
    }

    @NotNull
    public PsiInnerModule createPsi(@NotNull PsiModuleStub stub) {
        return new PsiInnerModuleImpl(stub, this);
    }
}
