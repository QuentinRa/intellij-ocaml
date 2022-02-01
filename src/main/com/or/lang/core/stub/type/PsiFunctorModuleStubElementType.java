package com.or.lang.core.stub.type;

import com.intellij.lang.ASTNode;
import com.or.lang.core.psi.PsiFunctor;
import com.or.lang.core.psi.impl.PsiFunctorImpl;
import com.or.lang.core.stub.PsiModuleStub;
import org.jetbrains.annotations.NotNull;

public class PsiFunctorModuleStubElementType extends PsiModuleStubElementType {
    public PsiFunctorModuleStubElementType() {
        super("C_FUNCTOR_DECLARATION");
    }

    @NotNull
    public PsiFunctor createPsi(@NotNull final PsiModuleStub stub) {
        return new PsiFunctorImpl(stub, this);
    }

    @NotNull
    public PsiFunctor createPsi(@NotNull ASTNode node) {
        return new PsiFunctorImpl(node);
    }
}
