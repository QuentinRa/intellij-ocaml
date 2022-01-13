package com.ocaml.ide.projectWizard;

import com.intellij.ide.util.projectWizard.*;
import com.ocaml.ide.sdk.*;

public class OCamlCompilerWizardStep extends ProjectJdkForModuleStep {
    private final OCamlModuleBuilder myBuilder;

    public OCamlCompilerWizardStep(WizardContext context, OCamlModuleBuilder builder) {
        super(context, OCamlSdkType.getInstance());
        myBuilder = builder;
    }

    @Override public void updateDataModel() {
        super.updateDataModel();
        myBuilder.setModuleJdk(getJdk());
    }
}
