package com.ocaml.ide.projectWizard;

import com.intellij.ide.util.projectWizard.*;
import com.intellij.openapi.*;
import com.intellij.openapi.module.*;
import com.intellij.openapi.options.*;
import com.intellij.openapi.projectRoots.*;
import com.intellij.openapi.roots.*;
import com.ocaml.ide.module.*;
import com.ocaml.ide.sdk.*;
import org.jetbrains.annotations.*;

/**
 * Set up an OCaml module/project.
 *
 * ---- getWeight
 * You may use this to move this module
 * to the top in the list, if needed (ex: 100).
 *
 * -- getGroupName / getParentGroup
 * You may use this if this module is inside a group.
 */
public class OCamlModuleBuilder extends ModuleBuilder {

    @Override public ModuleType<OCamlModuleBuilder> getModuleType() {
        return new OCamlModuleType();
    }

    @Override public boolean isSuitableSdkType(SdkTypeId sdkType) {
        return sdkType instanceof OCamlSdkType;
    }

    /**
     * todo: set sdk
     * todo: create src
     * todo: set output directory
     * todo: exclude output directory
     */
    @Override public void setupRootModel(@NotNull ModifiableRootModel modifiableRootModel) throws ConfigurationException {
        super.setupRootModel(modifiableRootModel);
    }

    @Nullable @Override public ModuleWizardStep getCustomOptionsStep(WizardContext context, Disposable parentDisposable) {
        return new OCamlSdkWizardStep(context, this);
    }
}