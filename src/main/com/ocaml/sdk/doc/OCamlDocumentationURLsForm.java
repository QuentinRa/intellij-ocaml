package com.ocaml.sdk.doc;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.ui.components.ActionLink;
import com.ocaml.sdk.OCamlSdkType;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Show the form to change the manual/API URL
 * (a.k.a. documentation URLs).
 */
public class OCamlDocumentationURLsForm {

    private JPanel myMainPanel;
    private JTextField myDocumentationURL;
    private JTextField myApiURL;
    private ActionLink myDocBefore;
    private ActionLink myDocAfter;
    private ActionLink myApiBefore;
    private ActionLink myApiAfter;

    private OCamlSdkAdditionalData myData;

    public void createUIComponents(@NotNull Sdk mySdk) {
        myData = (OCamlSdkAdditionalData) mySdk.getSdkAdditionalData();
        if (myData == null) return; // should never happen
        myDocumentationURL.setText(myData.ocamlManualURL);
        myApiURL.setText(myData.ocamlApiURL);
    }

    public JComponent getComponent() {
        return myMainPanel;
    }

    public boolean isModified() {
        if (myData != null) {
            return !myDocumentationURL.getText().trim().equals(myData.ocamlManualURL)
                    || !myApiURL.getText().trim().equals(myData.ocamlApiURL);
        }
        return false;
    }

    @SuppressWarnings("RedundantThrows")
    public void apply() throws ConfigurationException {
        if (myData != null) {
            myData.ocamlManualURL = myDocumentationURL.getText().trim();
            myData.ocamlApiURL = myApiURL.getText().trim();
        }
    }

    private void createUIComponents() {
        String docBefore = OCamlSdkType.getManualURL("4.10");
        String docAfter = OCamlSdkType.getManualURL("4.12");
        String apiBefore = OCamlSdkType.getApiURL("4.10");
        String apiAfter = OCamlSdkType.getApiURL("4.12");

        myDocBefore = new ActionLink(docBefore, event -> { BrowserUtil.browse(docBefore); });
        myDocBefore.setExternalLinkIcon();

        myDocAfter = new ActionLink(docAfter, event -> { BrowserUtil.browse(docAfter); });
        myDocAfter.setExternalLinkIcon();

        myApiBefore = new ActionLink(apiBefore, event -> { BrowserUtil.browse(apiBefore); });
        myApiBefore.setExternalLinkIcon();

        myApiAfter = new ActionLink(apiAfter, event -> { BrowserUtil.browse(apiAfter); });
        myApiAfter.setExternalLinkIcon();
    }
}
