package com.or.ide.insight;

import com.intellij.codeInsight.completion.*;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import com.or.ide.insight.provider.DotExpressionCompletionProvider;
import com.or.ide.insight.provider.FreeExpressionCompletionProvider;
import com.or.ide.insight.provider.ModuleCompletionProvider;
import com.or.ide.insight.provider.ObjectCompletionProvider;
import com.or.lang.OCamlTypes;
import com.or.lang.core.psi.PsiInclude;
import com.or.lang.core.psi.PsiOpen;
import com.or.lang.utils.QNameFinder;
import com.or.utils.Log;
import org.jetbrains.annotations.NotNull;

abstract class ORCompletionContributor extends CompletionContributor {
    static final Log LOG = Log.create("insight");

    ORCompletionContributor(@NotNull QNameFinder qnameFinder) {
        extend(CompletionType.BASIC, com.intellij.patterns.PlatformPatterns.psiElement(),
                new CompletionProvider<>() {
                    @Override
                    protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context, @NotNull CompletionResultSet result) {
                        PsiElement position = parameters.getPosition();
                        PsiElement originalPosition = parameters.getOriginalPosition();
                        PsiElement element = originalPosition == null ? position : originalPosition;
                        PsiElement prevLeaf = PsiTreeUtil.prevVisibleLeaf(element);
                        IElementType prevNodeType = prevLeaf == null ? null : prevLeaf.getNode().getElementType();
                        PsiElement parent = element.getParent();
                        PsiElement grandParent = parent == null ? null : parent.getParent();

                        if (LOG.isTraceEnabled()) {
                            LOG.debug("»» Completion: position: " + position + ", " + position.getText());
                            LOG.debug(
                                    "               original: "
                                            + originalPosition
                                            + ", "
                                            + (originalPosition == null ? null : originalPosition.getText()));
                            LOG.debug("                element: " + element);
                            LOG.debug("                 parent: " + parent);
                            LOG.debug("           grand-parent: " + grandParent);
                            LOG.debug("                   file: " + parameters.getOriginalFile());
                        }

                        // A comment, stop completion
                        if (element instanceof PsiComment) {
                            LOG.debug("comment, stop");
                            return;
                        }

                        // Just after an open/include keyword
                        if (prevNodeType == OCamlTypes.OPEN || prevNodeType == OCamlTypes.INCLUDE) {
                            LOG.debug("the previous keyword is OPEN/INCLUDE");
                            ModuleCompletionProvider.addCompletions(element, result);
                            return;
                        }
                        if (parent instanceof PsiOpen
                                || parent instanceof PsiInclude
                                || grandParent instanceof PsiOpen
                                || grandParent instanceof PsiInclude) {
                            LOG.debug("Inside OPEN/INCLUDE");
                            ModuleCompletionProvider.addCompletions(element, result);
                            return;
                        }

                        // Just after a DOT
                        if (prevNodeType == OCamlTypes.DOT) {
                            // But not in a guaranteed uncurried function
                            PsiElement prevPrevLeaf = prevLeaf.getPrevSibling();
                            if (prevPrevLeaf != null && prevPrevLeaf.getNode().getElementType() != OCamlTypes.LPAREN) {
                                LOG.debug("the previous element is DOT");

                                DotExpressionCompletionProvider.addCompletions(element, result);
                                return;
                            }
                        }

                        if (prevNodeType == OCamlTypes.SHARPSHARP) {
                            LOG.debug("the previous element is SHARPSHARP");
                            ObjectCompletionProvider.addCompletions(element, result);
                            return;
                        }

                        LOG.debug("Nothing found, free expression");
                        FreeExpressionCompletionProvider.addCompletions(qnameFinder, element, result);
                    }
                });
    }
}
