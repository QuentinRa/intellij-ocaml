package com.ocaml.ide.actions.editor;

import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.SdkAdditionalData;
import com.ocaml.ide.actions.editor.help.OCamlBaseOpenLikeAction;
import com.ocaml.sdk.OCamlSdkType;
import com.ocaml.sdk.doc.OCamlSdkAdditionalData;

public class OCamlEditorHelpAction extends OCamlBaseOpenLikeAction {

    public static final String ACTION_ID = "editor.help.action";

    @Override protected String getURL(Sdk sdk) {
        SdkAdditionalData sdkAdditionalData = sdk.getSdkAdditionalData();
        String url = null;
        if (sdkAdditionalData != null) {
            url = ((OCamlSdkAdditionalData) sdkAdditionalData).ocamlManualURL;
            url = url != null && url.startsWith("http") ? url : null;
        }
        if (url == null) url = OCamlSdkType.getInstance().getDefaultDocumentationUrl(sdk);
        return url;
    }
}
