package com.reason.ide.match;

import com.intellij.lang.*;
import com.intellij.psi.*;
import com.intellij.psi.tree.*;
import com.ocaml.lang.core.psi.*;
import org.jetbrains.annotations.*;

public class OclPairedBraceMatcher implements PairedBraceMatcher {
    private static final BracePair[] PAIRS =
            new BracePair[]{ //
                    new BracePair(OclTypes.LBRACE, OclTypes.RBRACE, false), //
                    new BracePair(OclTypes.BEGIN, OclTypes.END, false), //
                    new BracePair(OclTypes.STRUCT, OclTypes.END, false), //
                    new BracePair(OclTypes.SIG, OclTypes.END, false), //
                    new BracePair(OclTypes.OBJECT, OclTypes.END, false), //
                    new BracePair(OclTypes.LPAREN, OclTypes.RPAREN, false),
                    new BracePair(OclTypes.LBRACKET, OclTypes.RBRACKET, false),
                    new BracePair(OclTypes.LARRAY, OclTypes.RARRAY, false), //
                    new BracePair(OclTypes.LT, OclTypes.GT, false), //
                    new BracePair(OclTypes.DO, OclTypes.DONE, false), //
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