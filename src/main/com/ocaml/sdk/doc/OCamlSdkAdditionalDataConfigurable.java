package com.ocaml.sdk.doc;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.projectRoots.AdditionalDataConfigurable;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.SdkAdditionalData;
import com.intellij.openapi.projectRoots.impl.ProjectJdkImpl;
import com.ocaml.sdk.OCamlSdkType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * When we are in Project Structures > SDKs, setSdk will be called for every OCaml
 * Sdk in the list. We are supposed to create a view for the additional data, for each SDK.
 */
public class OCamlSdkAdditionalDataConfigurable implements AdditionalDataConfigurable {
    private final @NotNull OCamlDocumentationURLsForm myView = new OCamlDocumentationURLsForm();
    private @Nullable Sdk mySdk = null;

    @Override public @Nullable String getTabName() {
        return "Documentation";
    }

    @Override public void setSdk(@NotNull Sdk sdk) {
        mySdk = sdk;
        SdkAdditionalData data = sdk.getSdkAdditionalData();
        if (data == null) {
            data = new OCamlSdkAdditionalData();
            OCamlSdkType instance = OCamlSdkType.getInstance();
            ((OCamlSdkAdditionalData) data).ocamlManualURL = instance.getDefaultDocumentationUrl(sdk);
            ((OCamlSdkAdditionalData) data).ocamlApiURL = instance.getDefaultAPIUrl(sdk);
            ((ProjectJdkImpl) sdk).setSdkAdditionalData(data);
        }
    }

    @Override public @Nullable JComponent createComponent() {
        if (mySdk != null) {
            myView.createUIComponents(mySdk);
            return myView.getComponent();
        }
        return null;
    }

    @Override public boolean isModified() {
        return myView.isModified();
    }

    @Override public void apply() throws ConfigurationException {
        myView.apply();
    }
}
