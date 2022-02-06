package com.ocaml.ide;

import com.intellij.openapi.help.WebHelpProvider;
import com.ocaml.OCamlPluginConstants;
import com.ocaml.sdk.repl.OCamlREPLConstants;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * provide custom context web help
 */
public class OCamlWebHelpProvider extends WebHelpProvider {
    public static final String OCAML_REPL_HELP = OCamlPluginConstants.PLUGIN_ID + ".OCAML.REPL_HELP";

    @Override public @Nullable String getHelpPageUrl(@NotNull String helpTopicId) {
        if (helpTopicId.equals(OCAML_REPL_HELP)) {
            return OCamlREPLConstants.HELP_URL;
        }
        return null;
    }
}
