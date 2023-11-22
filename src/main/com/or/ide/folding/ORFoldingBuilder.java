package com.or.ide.folding;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilderEx;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.or.lang.OCamlTypes;
import com.or.lang.core.ORUtil;
import com.or.lang.core.psi.*;
import com.or.lang.core.psi.impl.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ORFoldingBuilder extends FoldingBuilderEx {
    @Override
    public FoldingDescriptor @NotNull [] buildFoldRegions(@NotNull PsiElement root, @NotNull Document document, boolean quick) {
        List<FoldingDescriptor> descriptors = new ArrayList<>();
        PsiTreeUtil.processElements(root, element -> {
            if (element instanceof PsiLet) {
                foldLet(descriptors, (PsiLet) element);
            } else if (element instanceof PsiType) {
                foldType(descriptors, (PsiType) element);
            } else if (element instanceof PsiInnerModule) {
                foldModule(descriptors, (PsiInnerModule) element);
            } else if (element instanceof PsiFunction) {
                foldFunction(descriptors, (PsiFunction) element);
            } else if (element instanceof PsiFunctor) {
                foldFunctor(descriptors, (PsiFunctor) element);
            } else if (element instanceof PsiTag) {
                foldTag(descriptors, (PsiTag) element);
            } else if (element instanceof PsiPatternMatch) {
                foldPatternMatch(descriptors, (PsiPatternMatch) element);
            } else if (element instanceof PsiSwitch) {
                foldSwitch(descriptors, (PsiSwitch) element);
            } else if (OCamlTypes.MULTI_COMMENT == element.getNode().getElementType()) {
                FoldingDescriptor fold = fold(element);
                if (fold != null) {
                    descriptors.add(fold);
                }
            }

            return true;
        });

        return descriptors.toArray(new FoldingDescriptor[0]);
    }

    private void foldLet(@NotNull List<FoldingDescriptor> descriptors, @NotNull PsiLet letExpression) {
        FoldingDescriptor fold = fold(letExpression.getBinding());
        if (fold != null) {
            descriptors.add(fold);
        }
    }

    private void foldType(@NotNull List<FoldingDescriptor> descriptors, @NotNull PsiType typeExpression) {
        PsiElement constrName = ORUtil.findImmediateFirstChildOfClass(typeExpression, PsiLowerIdentifier.class);
        if (constrName != null) {
            PsiElement binding = typeExpression.getBinding();
            if (binding != null && binding.getTextLength() > 5) {
                descriptors.add(new FoldingDescriptor(typeExpression, binding.getTextRange()));
            }
        }
    }

    private void foldModule(@NotNull List<FoldingDescriptor> descriptors, @NotNull PsiInnerModule module) {
        FoldingDescriptor foldSignature = fold(module.getModuleType());
        if (foldSignature != null) {
            descriptors.add(foldSignature);
        }

        FoldingDescriptor foldBody = fold(module.getBody());
        if (foldBody != null) {
            descriptors.add(foldBody);
        }
    }

    private void foldFunction(@NotNull List<FoldingDescriptor> descriptors, @NotNull PsiFunction func) {
        FoldingDescriptor foldBinding = fold(func.getBody());
        if (foldBinding != null) {
            descriptors.add(foldBinding);
        }
    }

    private void foldFunctor(@NotNull List<FoldingDescriptor> descriptors, @NotNull PsiFunctor element) {
        FoldingDescriptor foldBinding = fold(element.getBody());
        if (foldBinding != null) {
            descriptors.add(foldBinding);
        }
    }

    private void foldTag(@NotNull List<FoldingDescriptor> descriptors, @NotNull PsiTag element) {
        PsiTagStart start = ORUtil.findImmediateFirstChildOfClass(element, PsiTagStart.class);
        PsiTagClose close = start == null ? null : ORUtil.findImmediateFirstChildOfClass(element, PsiTagClose.class);
        // Auto-closed tags are not foldable
        if (close != null) {
            PsiElement lastChild = start.getLastChild();
            TextRange textRange = TextRange.create(lastChild.getTextOffset(), element.getTextRange().getEndOffset() - 1);
            descriptors.add(new FoldingDescriptor((PsiElement) element, textRange));
        }
    }

    private void foldSwitch(@NotNull List<FoldingDescriptor> descriptors, @NotNull PsiSwitch element) {
        PsiBinaryCondition condition = element.getCondition();
        if (condition != null) {
            int startOffset = condition.getTextOffset() + condition.getTextLength() + 1;
            int endOffset = element.getTextRange().getEndOffset();
            if (startOffset < endOffset) {
                TextRange textRange = TextRange.create(startOffset, endOffset);
                descriptors.add(new FoldingDescriptor(element, textRange));
            }
        }
    }

    private void foldPatternMatch(@NotNull List<FoldingDescriptor> descriptors, @NotNull PsiPatternMatch element) {
        FoldingDescriptor fold = fold(element.getBody());
        if (fold != null) {
            descriptors.add(fold);
        }
    }

    @Nullable
    @Override
    public String getPlaceholderText(@NotNull ASTNode node) {
        IElementType elementType = node.getElementType();
        if (elementType == OCamlTypes.MULTI_COMMENT) {
            return "(*...*)";
        } else if (elementType == OCamlTypes.C_MODULE_TYPE) {
            return "sig...";
        }
        return "...";
    }

    @Override
    public boolean isCollapsedByDefault(@NotNull ASTNode node) {
        return false;
    }

    @Nullable
    private FoldingDescriptor fold(@Nullable PsiElement element) {
        if (element == null) {
            return null;
        }
        TextRange textRange = element.getTextRange();
        return textRange.getLength() > 5 ? new FoldingDescriptor(element, textRange) : null;
    }
}
