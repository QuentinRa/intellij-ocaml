package com.or.ide.hints;

import com.intellij.openapi.util.Key;
import com.or.lang.core.psi.PsiSignature;
import org.jetbrains.annotations.Nullable;

// just an experiment, cancelled for now
public class SignatureProvider /*implements InlayParameterHintsProvider*/ {
    public static final Key<InferredTypesWithLines> SIGNATURES_CONTEXT = Key.create("REASONML_SIGNATURES_CONTEXT");

    private SignatureProvider() {
    }

    public static class InferredTypesWithLines {
        public @Nullable PsiSignature getSignatureByOffset(int textOffset) {
            return null;
        }
    }
}
