package com.dune.lang.parser;

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

public class DuneParser implements PsiParser {

    @Override public @NotNull ASTNode parse(@NotNull IElementType root, @NotNull PsiBuilder builder) {
        return null;
    }
}
