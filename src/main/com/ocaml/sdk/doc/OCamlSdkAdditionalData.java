package com.ocaml.sdk.doc;

import com.intellij.openapi.projectRoots.SdkAdditionalData;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class OCamlSdkAdditionalData implements SdkAdditionalData {

    /**
     * URL to the manual of the current SDK version of OCaml
     */
    public String ocamlManualURL = "";
    /**
     * URL to the API of the current SDK version of OCaml
     */
    public String ocamlApiURL = "";

    // utils

    @Contract(pure = true) @Override public @NotNull String toString() {
        return "OCamlSdkAdditionalData{" +
                "ocamlManualURL='" + ocamlManualURL + '\'' +
                ", ocamlApiURL='" + ocamlApiURL + '\'' +
                '}';
    }

    public boolean shouldFillWithDefaultValues() {
        // if empty, yes
        return ocamlApiURL == null || ocamlManualURL == null ||
                ocamlManualURL.isBlank() || ocamlApiURL.isBlank();
    }
}
