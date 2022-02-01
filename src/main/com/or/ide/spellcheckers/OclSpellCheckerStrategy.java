package com.or.ide.spellcheckers;

import com.intellij.psi.PsiElement;
import com.intellij.spellchecker.tokenizer.Tokenizer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OclSpellCheckerStrategy extends ORSpellCheckerStrategy {
    @Override
    public @NotNull Tokenizer<?> getTokenizer(@Nullable PsiElement element) {
        return super.getTokenizer(element);
    }
}
