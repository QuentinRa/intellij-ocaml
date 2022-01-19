package com.ocaml.ide.sdk;

import com.intellij.openapi.projectRoots.*;
import org.jetbrains.annotations.*;

/**
 * I'm still unsure of which kind of JDK we will use as a base for OCamlSdkType.
 * This class is the one handling this.
 */
abstract class LocalSdkType extends SdkType {
    public LocalSdkType(@NotNull String name) {
        super(name);
    }
}
