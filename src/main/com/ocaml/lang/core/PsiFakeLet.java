package com.ocaml.lang.core;

import com.intellij.psi.impl.source.tree.CompositePsiElement;
import com.or.lang.core.psi.PsiFunction;
import com.or.lang.core.psi.PsiParameter;
import com.or.lang.core.psi.impl.PsiFunctionImpl;
import com.or.lang.core.psi.impl.PsiLetImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * An implementation used to convert a list of parameters
 * to a let expression that is a function. Can only be used in
 * OCamlParameterNameHints.java for now.
 * @see com.ocaml.ide.insight.OCamlParameterNameHints
 */
public class PsiFakeLet extends PsiLetImpl {
    private final PsiFunction myFunction;

    public PsiFakeLet(@NotNull List<PsiParameter> params) {
        super(new CompositePsiElement(new OCamlTokenType("PsiFakeLet")) {});
        myFunction = params.isEmpty() ? null : new FakePsiFunction(params);
    }

    @Override public boolean isFunction() {
        return myFunction != null;
    }

    @Override public @Nullable PsiFunction getFunction() {
        return myFunction;
    }

    private static final class FakePsiFunction extends PsiFunctionImpl {
        private final List<PsiParameter> myParams;

        private FakePsiFunction(List<PsiParameter> params) {
            super(new OCamlTokenType("PsiFakeFunction") {});
            this.myParams = params;
        }

        @Override public @NotNull List<PsiParameter> getParameters() {
            return myParams;
        }
    }
}
