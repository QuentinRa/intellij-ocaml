package com.ocaml.ide.actions.editor;

import com.intellij.openapi.projectRoots.Sdk;
import com.ocaml.ide.actions.editor.help.OCamlBaseOpenLikeAction;
import com.ocaml.sdk.OCamlSdkType;

public class OCamlEditorApiAction extends OCamlBaseOpenLikeAction {

    public static final String ACTION_ID = "editor.api.action";

    @Override protected String getURL(Sdk sdk) {
        return OCamlSdkType.getInstance().getRealAPIURL(sdk);
    }
}
