package com.ocaml.lang.utils;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.or.lang.core.ORUtil;
import com.or.lang.core.psi.PsiFunction;
import com.or.lang.core.psi.PsiLet;
import com.or.lang.core.psi.PsiStructuredElement;
import com.or.lang.core.psi.impl.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OCamlElementVisitor extends PsiElementVisitor {
    private final List<PsiElement> m_treeElements;
    private final int m_elementLevel;

    public OCamlElementVisitor(List<PsiElement> elements, int elementLevel) {
        m_treeElements = elements;
        m_elementLevel = elementLevel;
    }

    @Override
    public void visitElement(@NotNull PsiElement element) {
        if (element instanceof PsiStructuredElement && !(element instanceof PsiFakeModule)) {
            if (element instanceof PsiLet) {
                PsiLet let = (PsiLet) element;
                if (let.isScopeIdentifier()) {
                    // it's a tuple! add each element of the tuple separately.
                    for (PsiElement child : let.getScopeChildren()) {
                        if (child instanceof PsiLowerIdentifier) {
                            m_treeElements.add(child);
                        }
                    }
                }
            }
            m_treeElements.add(element);
        } else if (element instanceof PsiRecord) {
            m_treeElements.addAll(((PsiRecord) element).getFields());
        } else if (element instanceof PsiScopedExpr && m_elementLevel < 2) {
            List<PsiStructuredElement> children = ORUtil.findImmediateChildrenOfClass(element, PsiStructuredElement.class);
            for (PsiStructuredElement child : children) {
                if (child.canBeDisplayed()) {
                    m_treeElements.add(child);
                }
            }
        } else if (element instanceof PsiFunction && m_elementLevel < 2) {
            PsiFunctionBody body = ((PsiFunction) element).getBody();
            List<PsiStructuredElement> children = ORUtil.findImmediateChildrenOfClass(body, PsiStructuredElement.class);
            for (PsiStructuredElement child : children) {
                if (child.canBeDisplayed()) {
                    m_treeElements.add(child);
                }
            }
        }
    }
}