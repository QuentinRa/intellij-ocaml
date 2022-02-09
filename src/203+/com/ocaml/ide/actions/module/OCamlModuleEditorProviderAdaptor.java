package com.ocaml.ide.actions.module;

import com.intellij.openapi.module.GeneralModuleType;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.roots.ui.configuration.ModuleConfigurationState;
import com.ocaml.ide.module.OCamlModuleType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The check in this method changed in 211 (included), but we can't handle this fine.
 * Unfortunately, in 203, the method to get the ModifiableRootModel does not exists, so
 * that why we need this class.
 */
public class OCamlModuleEditorProviderAdaptor {

    public static @Nullable Module getModuleFromState(@NotNull ModuleConfigurationState state) {
                Module module = state.getCurrentRootModel().getModule();
        ModuleType<?> moduleType = ModuleType.get(module);

        // We are not handling non-ocaml modules
        if (!(moduleType instanceof OCamlModuleType) &&
                (!GeneralModuleType.INSTANCE.equals(moduleType) || ProjectRootManager.getInstance(state.getProject()).getProjectSdk() == null)) {
            return null;
        }
        return module;
    }

}
