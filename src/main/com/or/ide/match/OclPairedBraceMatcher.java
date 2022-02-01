package com.or.ide.match;

import com.intellij.lang.BracePair;
import com.intellij.lang.PairedBraceMatcher;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.or.lang.OCamlTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OclPairedBraceMatcher implements PairedBraceMatcher {
    private static final BracePair[] PAIRS =
            new BracePair[]{ //
                    new BracePair(OCamlTypes.LBRACE, OCamlTypes.RBRACE, false), //
                    new BracePair(OCamlTypes.BEGIN, OCamlTypes.END, false), //
                    new BracePair(OCamlTypes.STRUCT, OCamlTypes.END, false), //
                    new BracePair(OCamlTypes.SIG, OCamlTypes.END, false), //
                    new BracePair(OCamlTypes.OBJECT, OCamlTypes.END, false), //
                    new BracePair(OCamlTypes.LPAREN, OCamlTypes.RPAREN, false),
                    new BracePair(OCamlTypes.LBRACKET, OCamlTypes.RBRACKET, false),
                    new BracePair(OCamlTypes.LARRAY, OCamlTypes.RARRAY, false), //
                    new BracePair(OCamlTypes.LT, OCamlTypes.GT, false), //
                    new BracePair(OCamlTypes.DO, OCamlTypes.DONE, false), //
            };

    @Override
    public BracePair @NotNull [] getPairs() {
        return PAIRS;
    }

    @Override
    public boolean isPairedBracesAllowedBeforeType(@NotNull IElementType lbraceType, @Nullable IElementType contextType) {
        return true;
    }

    @Override
    public int getCodeConstructStart(PsiFile file, int openingBraceOffset) {
        return openingBraceOffset;
    }
}
