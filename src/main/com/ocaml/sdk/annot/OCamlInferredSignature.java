package com.ocaml.sdk.annot;

import com.intellij.build.FilePosition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OCamlInferredSignature {

    /**
     * Name, not null for variables/modules
     */
    @Nullable public String name;

    /**
     * The kind of item that has this type
     */
    @NotNull public Kind kind = Kind.UNKNOWN;
    public enum Kind {
        UNKNOWN, VALUE, VARIABLE, MODULE
    }

    /**
     * The type of this item
     */
    @NotNull public String type = "";

    /**
     * Location of this error
     */
    @NotNull public FilePosition position = new FilePosition(null, 0,0);
}
