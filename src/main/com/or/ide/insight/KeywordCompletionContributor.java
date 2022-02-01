package com.or.ide.insight;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.openapi.project.DumbAware;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.ProcessingContext;
import com.or.ide.files.FileBase;
import com.or.lang.OCamlTypes;
import com.or.utils.Log;
import org.jetbrains.annotations.NotNull;

import static com.intellij.patterns.PlatformPatterns.psiElement;

abstract class KeywordCompletionContributor extends CompletionContributor implements DumbAware {
    protected static final InsertHandler<LookupElement> INSERT_SPACE = new AddSpaceInsertHandler(false);
    private static final Log LOG = Log.create("insight.keyword");

    KeywordCompletionContributor() {
        extend(CompletionType.BASIC, psiElement(), new CompletionProvider<>() {
            @Override
            protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context, @NotNull CompletionResultSet result) {
                PsiElement position = parameters.getPosition();
                PsiElement originalPosition = parameters.getOriginalPosition();
                PsiElement element = originalPosition == null ? position : originalPosition;
                IElementType prevNodeType = CompletionUtils.getPrevNodeType(element);
                PsiElement parent = element.getParent();
                PsiElement grandParent = parent == null ? null : parent.getParent();

                if (LOG.isTraceEnabled()) {
                    LOG.trace("»» Completion: position: " + position + ", " + position.getText());
                    LOG.trace("               original: " + originalPosition + ", " + (originalPosition == null ? null : originalPosition.getText()));
                    LOG.trace("                element: " + element);
                    LOG.trace("                 parent: " + parent);
                    LOG.trace("           grand-parent: " + grandParent);
                    LOG.trace("                   file: " + parameters.getOriginalFile());
                }

                if (originalPosition == null && parent instanceof FileBase) {
                    if (prevNodeType != OCamlTypes.DOT
                            && prevNodeType != OCamlTypes.SHARPSHARP
                            && prevNodeType != OCamlTypes.C_LOWER_SYMBOL) {
                        addFileKeywords(result);
                    }
                }
            }
        });
    }

    protected abstract void addFileKeywords(@NotNull CompletionResultSet result);
}
