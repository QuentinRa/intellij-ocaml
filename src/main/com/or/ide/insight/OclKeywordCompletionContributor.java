package com.or.ide.insight;

import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.PrioritizedLookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import org.jetbrains.annotations.NotNull;

import static com.or.ide.insight.CompletionUtils.KEYWORD_PRIORITY;

public class OclKeywordCompletionContributor extends KeywordCompletionContributor {
    private static final String[] KEYWORDS =
            new String[]{"open", "include", "module", "type", "let", "external", "exception"};

    OclKeywordCompletionContributor() {
        super();
    }

    @Override
    protected void addFileKeywords(@NotNull CompletionResultSet result) {
        for (String keyword : KEYWORDS) {
            LookupElementBuilder builder =
                    LookupElementBuilder.create(keyword).withInsertHandler(INSERT_SPACE).bold();

            result.addElement(PrioritizedLookupElement.withPriority(builder, KEYWORD_PRIORITY));
        }
    }
}
