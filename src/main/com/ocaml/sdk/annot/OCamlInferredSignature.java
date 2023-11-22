package com.ocaml.sdk.annot;

import com.intellij.build.FilePosition;
import com.intellij.openapi.util.TextRange;
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

    @NotNull public TextRange range = new TextRange(0, 0);


    @Override public String toString() {
        return "OCamlInferredSignature{" +
                "name='" + name + '\'' +
                ", kind=" + kind +
                ", type='" + type + '\'' +
                ", file=" + position.getFile() +
                "(l:" + position.getStartLine() + "-" + position.getEndLine() + ", c:"
                + position.getStartColumn() + "-" + position.getEndColumn() + ")" +
                ", range=" + range +
                '}';
    }
}
