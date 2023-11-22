package com.ocaml.ide.settings;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Create a file in .idea called ocaml.xml.
 * This file is only created if the user defined values that are
 * different of the default ones.
 */
@State(name = "OCamlSettings", storages = {@Storage("ocaml.xml")})
public class OCamlSettings implements PersistentStateComponent<OCamlSettings> {

    public static OCamlSettings getInstance(@NotNull Project project) {
        return project.getService(OCamlSettings.class);
    }

    /**
     * For Non-IntelliJ compatibles editors, this is the name of the
     * outputFolder, relative to the project root.
     */
    public String outputFolderName = "out/";

    @Override public @Nullable OCamlSettings getState() {
        return this;
    }

    @Override public void loadState(@NotNull OCamlSettings state) {
        outputFolderName = state.outputFolderName;
    }
}