package com.ocaml.comp.opam.process;

import com.intellij.execution.*;
import com.intellij.execution.configurations.*;
import com.intellij.execution.process.*;
import com.intellij.openapi.util.*;
import com.ocaml.ide.sdk.*;
import com.ocaml.utils.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class FindSwitchProcess {

    public static GeneralCommandLine getCommand() {
        return new GeneralCommandLine("opam", "switch", "list", "--color=never");
    }

    public static class OpamSwitch {
        public final boolean isSelected;
        public final String name;
        public final String path;

        private OpamSwitch(boolean isSelected, String opamHome, String name) {
            this.isSelected = isSelected;
            this.name = name;
            this.path = opamHome + name;
        }

        @Override public String toString() {
            return (isSelected ? ">" : "") + name;
        }
    }

    public static void findSwitch(@NotNull String opamHome, @NotNull GeneralCommandLine cli,
                                  @NotNull ProcessCallback<List<OpamSwitch>> onProcessTerminated) {
        List<OpamSwitch> result = new ArrayList<>();
        KillableProcessHandler processHandler;
        try {
            processHandler = new KillableProcessHandler(cli);
            processHandler.addProcessListener(new ProcessListener() {
                private boolean isHeader = true;
                private boolean isFooter = false;

                @Override public void startNotified(@NotNull ProcessEvent event) {}

                @Override public void processTerminated(@NotNull ProcessEvent event) {
                    onProcessTerminated.call(result);
                }

                @Override
                public void onTextAvailable(@NotNull ProcessEvent event, @NotNull Key outputType) {
                    if (ProcessOutputType.isStdout(outputType)) {
                        String text = event.getText();
                        if (isHeader) {
                            isHeader = false;
                        } else if (text.startsWith("[WARNING]")) {
                            isFooter = true;
                        } else if (!isFooter) {
                            String[] tokens = text.split("\\s+");
                            if (tokens.length == 4) {
                                String name = tokens[1];
                                if (OCamlSdkType.VERSION_REGEXP.matcher("./"+name).matches()) {
                                    result.add(new OpamSwitch(
                                            !tokens[0].isEmpty(),
                                            opamHome,
                                            name
                                    ));
                                }
                            }
                        }
                    }
                }
            });
            processHandler.startNotify();
        } catch (ExecutionException e) {
            onProcessTerminated.call(result);
        }
    }

}
