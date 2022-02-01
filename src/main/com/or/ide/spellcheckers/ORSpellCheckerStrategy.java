package com.or.ide.spellcheckers;

import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.spellchecker.tokenizer.SpellcheckingStrategy;
import com.intellij.spellchecker.tokenizer.Tokenizer;
import com.or.lang.core.psi.PsiLiteralExpression;
import com.or.lang.core.psi.PsiUpperSymbol;
import com.or.lang.core.psi.impl.PsiLowerIdentifier;
import com.or.lang.core.psi.impl.PsiLowerSymbolImpl;
import com.or.lang.core.psi.impl.PsiUpperIdentifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Handling the Generic SpellcheckingStrategy
 *
 * @see SpellcheckingStrategy
 */
public class ORSpellCheckerStrategy extends SpellcheckingStrategy {
    @Override
    public @NotNull Tokenizer<?> getTokenizer(@Nullable PsiElement element) {
        if (element == null || element instanceof PsiWhiteSpace) { // skip whitespace
            return EMPTY_TOKENIZER;
        }
        // optimization
        if (element.getClass() == LeafPsiElement.class) {
            return EMPTY_TOKENIZER;
        }
        // skip other languages
        if (element instanceof PsiLanguageInjectionHost && InjectedLanguageManager.getInstance(element.getProject()).getInjectedPsiFiles(element) != null) {
            return EMPTY_TOKENIZER;
        }
        // handle comments
        if (element instanceof PsiComment) {
            return myCommentTokenizer;
        }
        // literals
        if (element instanceof PsiLiteralExpression) {
            return TEXT_TOKENIZER;
        }
        // Named elements
        if (element instanceof PsiUpperIdentifier || element instanceof PsiUpperSymbol || element instanceof PsiLowerIdentifier || element instanceof PsiLowerSymbolImpl) {
            return TEXT_TOKENIZER;
        }
        return EMPTY_TOKENIZER; // skip everything else
    }
}
