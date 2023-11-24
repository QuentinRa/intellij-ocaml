package com.or.lang.core.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.util.IncorrectOperationException;
import com.ocaml.OCamlLanguage;
import com.or.lang.OCamlTypes;
import com.or.lang.core.ORUtil;
import com.or.lang.core.psi.PsiLanguageConverter;
import com.or.lang.core.psi.PsiQualifiedPathElement;
import com.or.lang.core.stub.PsiObjectFieldStub;
import com.or.lang.utils.ORLanguageProperties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PsiObjectField extends PsiTokenStub<PsiObjectField, PsiObjectFieldStub> implements PsiLanguageConverter, PsiQualifiedPathElement, StubBasedPsiElement<PsiObjectFieldStub> {
    // region Constructors
    public PsiObjectField(@NotNull ASTNode node) {
        super(node);
    }

    public PsiObjectField(@NotNull PsiObjectFieldStub stub, @NotNull IStubElementType nodeType) {
        super(stub, nodeType);
    }
    // endregion

    public @Nullable PsiElement getNameIdentifier() {
        return getFirstChild();
    }

    @Override
    public @NotNull String getName() {
        PsiElement nameElement = getNameIdentifier();
        return nameElement == null ? "" : nameElement.getText().replaceAll("\"", "");
    }

    @Override
    public @Nullable PsiElement setName(@NotNull String name) throws IncorrectOperationException {
        return null;
    }

    //region PsiQualifiedName
    @Override
    public String [] getPath() {
        PsiObjectFieldStub stub = getGreenStub();
        if (stub != null) {
            return stub.getPath();
        }

        return ORUtil.getQualifiedPath(this);
    }

    @Override
    public @NotNull String getQualifiedName() {
        PsiObjectFieldStub stub = getGreenStub();
        if (stub != null) {
            return stub.getQualifiedName();
        }

        return ORUtil.getQualifiedName(this);
    }
    //endregion

    @Override
    public @NotNull String asText(@Nullable ORLanguageProperties toLang) {
        StringBuilder convertedText = null;
        Language fromLang = getLanguage();

        if (fromLang != toLang) {
            if (toLang != OCamlLanguage.INSTANCE) {
                convertedText = new StringBuilder();

                // Convert from OCaml to Reason
                PsiElement nameIdentifier = getNameIdentifier();
                if (nameIdentifier == null) {
                    convertedText.append(getText());
                } else {
                    PsiElement value = getValue();

                    String valueAsText = "";
                    if (value instanceof PsiLanguageConverter) {
                        valueAsText = ((PsiLanguageConverter) value).asText(toLang);
                    } else if (value != null) {
                        valueAsText = value.getText();
                    }

                    convertedText.append(nameIdentifier.getText()).append(":").append(valueAsText);
                }
            }
        }

        return convertedText == null ? getText() : convertedText.toString();
    }

    @Nullable
    public PsiElement getValue() {
        PsiElement colon = ORUtil.findImmediateFirstChildOfType(this, OCamlTypes.COLON);
        return colon == null ? null : ORUtil.nextSiblingNode(colon.getNode()).getPsi();
    }

    @NotNull
    @Override
    public String toString() {
        return "ObjectField";
    }
}
