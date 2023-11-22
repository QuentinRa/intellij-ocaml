package com.ocaml.ide.actions.editor.help;

import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.SdkAdditionalData;
import com.ocaml.sdk.OCamlSdkType;
import com.ocaml.sdk.doc.OCamlSdkAdditionalData;

public class OCamlEditorApiAction extends OCamlBaseOpenLinkAction {

    public static final String ACTION_ID = "editor.api.action";

    @Override protected String getURL(Sdk sdk) {
        SdkAdditionalData sdkAdditionalData = sdk.getSdkAdditionalData();
        String url = null;
        if (sdkAdditionalData != null) {
            url = ((OCamlSdkAdditionalData) sdkAdditionalData).ocamlApiURL;
            url = url != null && url.startsWith("http") ? url : null;
        }
        if (url == null) url = OCamlSdkType.getInstance().getDefaultAPIUrl(sdk);
        return url;
    }
}
