package com.ocaml.utils;

import com.intellij.ide.BrowserUtil;
import com.intellij.ui.components.ActionLink;
import com.ocaml.OCamlBundle;
import com.ocaml.OCamlPluginConstants;
import org.jetbrains.annotations.NotNull;

/**
 * Generate action links
 */
public class OCamlBrowseUtil {
    public static @NotNull ActionLink toDocumentation() {
        ActionLink pluginDocumentation = new ActionLink(
                OCamlBundle.message("project.wizard.documentation.link"), event -> {
            BrowserUtil.browse(OCamlPluginConstants.DOCUMENTATION_LINK);
        });
        pluginDocumentation.setExternalLinkIcon();
        return pluginDocumentation;
    }

    public static @NotNull ActionLink toIssues() {
        ActionLink bugTracker = new ActionLink(OCamlBundle.message("project.wizard.issues.link"),
                event -> {
            BrowserUtil.browse(OCamlPluginConstants.ISSUES_LINK);
        });
        bugTracker.setExternalLinkIcon();
        return bugTracker;
    }
}
