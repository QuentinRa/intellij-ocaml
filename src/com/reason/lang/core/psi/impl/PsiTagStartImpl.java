package com.reason.lang.core.psi.impl;

import com.intellij.psi.*;
import com.intellij.psi.tree.*;
import com.intellij.psi.util.*;
import com.intellij.util.*;
import com.reason.lang.core.*;
import com.reason.lang.core.psi.*;
import com.reason.lang.core.psi.reference.*;
import com.reason.lang.core.type.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class PsiTagStartImpl extends CompositeTypePsiElement<ORTypes> implements PsiTagStart {
    protected PsiTagStartImpl(@NotNull ORTypes types, @NotNull IElementType elementType) {
        super(types, elementType);
    }

    public static @NotNull ComponentPropertyAdapter createProp(String name, String type) {
        return new ComponentPropertyAdapter(name, type);
    }

    @Override
    public @Nullable PsiElement getNameIdentifier() {
        PsiElement lastTag = null;

        Collection<PsiLeafTagName> tags = PsiTreeUtil.findChildrenOfType(this, PsiLeafTagName.class);
        if (!tags.isEmpty()) {
            for (PsiLeafTagName tag : tags) {
                PsiElement currentStart = tag.getParent().getParent();
                if (currentStart == this) {
                    lastTag = tag.getParent();
                }
            }
        }

        return lastTag;
    }

    @Override
    public @Nullable String getName() {
        PsiElement nameIdentifier = getNameIdentifier();
        return nameIdentifier == null ? null : nameIdentifier.getText();
    }

    @Override
    public @Nullable PsiElement setName(@NotNull String name) throws IncorrectOperationException {
        return null;
    }

    @Override
    public @NotNull List<PsiTagProperty> getProperties() {
        return ORUtil.findImmediateChildrenOfClass(this, PsiTagProperty.class);
    }

    @Override
    public @NotNull List<ComponentPropertyAdapter> getUnifiedPropertyList() {
        final List<ComponentPropertyAdapter> result = new ArrayList<>();

        // find tag 'make' expression
        PsiElement tagName = getNameIdentifier();
        if (tagName instanceof PsiUpperSymbol) {
            PsiUpperSymbolReference uReference = (PsiUpperSymbolReference) tagName.getReference();
            PsiElement uResolved = uReference == null ? null : uReference.resolveInterface();
            if (uResolved instanceof PsiLowerIdentifier) {
                PsiElement resolvedElement = uResolved.getParent();
                if (resolvedElement instanceof PsiLet) {
                    PsiFunction makeFunction = ((PsiLet) resolvedElement).getFunction();
                    if (makeFunction != null) {
                        makeFunction.getParameters().stream()
                                .filter(p -> !"children".equals(p.getName()) && !"_children".equals(p.getName()))
                                .forEach(p -> result.add(new ComponentPropertyAdapter(p)));
                    }
                } else if (resolvedElement instanceof PsiExternal) {
                    PsiSignature signature = ((PsiExternal) resolvedElement).getSignature();
                    if (signature != null) {
                        signature.getItems().stream()
                                .filter(p -> !"children".equals(p.getName()) && !"_children".equals(p.getName()))
                                .forEach(p -> result.add(new ComponentPropertyAdapter(p)));
                    }
                }
            }
        }

        return result;
    }

    @Override
    public @NotNull String toString() {
        return "Tag start";
    }
}
