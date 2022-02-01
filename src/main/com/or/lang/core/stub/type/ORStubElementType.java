package com.or.lang.core.stub.type;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import com.ocaml.OCamlLanguage;
import com.or.lang.core.type.ORCompositeType;
import org.jetbrains.annotations.NotNull;

public abstract class ORStubElementType<StubT extends StubElement<?>, PsiT extends PsiElement> extends IStubElementType<StubT, PsiT> implements ORCompositeType {
    static final String[] EMPTY_PATH = new String[0];

    ORStubElementType(@NotNull String debugName) {
        super(debugName, OCamlLanguage.INSTANCE);
    }

    public abstract @NotNull PsiElement createPsi(ASTNode node);
}
