package com.ocaml.ide.actions.editor.help;

import com.intellij.ide.browsers.BrowserLauncher;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.psi.PsiFile;
import com.intellij.ui.GotItTooltip;
import com.ocaml.OCamlBundle;
import com.ocaml.ide.actions.editor.OCamlEditorActionBase;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public abstract class OCamlBaseOpenLikeAction extends OCamlEditorActionBase {

    @Override public void actionPerformed(@NotNull AnActionEvent e) {
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        if (psiFile == null) return;
        Module module = ModuleUtilCore.findModuleForFile(psiFile);
        if (module == null) return;
        Sdk sdk = ModuleRootManager.getInstance(module).getSdk();
        if (sdk == null) return;
        String url = getURL(sdk);
        if (url == null) return;

        // show a tooltip, the first time
        GotItTooltip tooltip = new GotItTooltip(
                "ocaml.features.help", OCamlBundle.message("sdk.help.got.it.title"), e.getProject()
        ).withHeader(OCamlBundle.message("sdk.help.got.it.content"));
        tooltip.show((JComponent) e.getInputEvent().getComponent(), GotItTooltip.TOP_MIDDLE);

        BrowserLauncher.getInstance().browse(url, null);
    }

    protected abstract String getURL(Sdk sdk);
}
