package com.or.lang.core.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.util.IncorrectOperationException;
import com.or.lang.core.ORUtil;
import com.or.lang.core.psi.impl.PsiTokenStub;
import com.or.lang.core.psi.impl.PsiUpperIdentifier;
import com.or.lang.core.stub.PsiVariantDeclarationStub;
import org.jetbrains.annotations.NotNull;

public class PsiVariantDeclaration extends PsiTokenStub<PsiVariantDeclaration, PsiVariantDeclarationStub> implements PsiQualifiedPathElement, StubBasedPsiElement<PsiVariantDeclarationStub> {
    // region Constructors
    public PsiVariantDeclaration(@NotNull ASTNode node) {
        super(node);
    }

    public PsiVariantDeclaration(@NotNull PsiVariantDeclarationStub stub, @NotNull IStubElementType nodeType) {
        super(stub, nodeType);
    }
    // endregion

    //region PsiNamedElement
    @Override
    public String getName() {
        PsiVariantDeclarationStub stub = getGreenStub();
        if (stub != null) {
            return stub.getName();
        }

        PsiElement nameIdentifier = getFirstChild();
        return nameIdentifier == null ? "" : nameIdentifier.getText();
    }

    @Override
    public @NotNull PsiElement setName(@NotNull String name) throws IncorrectOperationException {
        return this;
    }
    //endregion

    //region PsiQualifiedName
    @Override
    public String [] getPath() {
        PsiVariantDeclarationStub stub = getGreenStub();
        if (stub != null) {
            return stub.getPath();
        }

        //noinspection ConstantConditions
        return ORUtil.getQualifiedPath(this);
    }

    @Override
    public @NotNull String getQualifiedName() {
        PsiVariantDeclarationStub stub = getGreenStub();
        if (stub != null) {
            return stub.getQualifiedName();
        }

        return ORUtil.getQualifiedName(this);
    }
    //endregion

    @Override
    public @NotNull PsiElement getNavigationElement() {
        PsiUpperIdentifier id = ORUtil.findImmediateFirstChildOfClass(this, PsiUpperIdentifier.class);
        return id == null ? this : id;
    }

    @NotNull
    @Override
    public String toString() {
        return "Variant declaration";
    }
}
