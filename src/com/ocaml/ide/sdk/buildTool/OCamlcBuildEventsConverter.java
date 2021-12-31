package com.ocaml.ide.sdk.buildTool;

import com.intellij.build.events.*;
import com.intellij.build.output.*;

import java.util.function.*;

public class OCamlcBuildEventsConverter implements BuildOutputParser {
    public OCamlcBuildEventsConverter(OCamlcBuildContext context) {
    }

    @Override public boolean parse(String line,
                                   BuildOutputInstantReader reader,
                                   Consumer<? super BuildEvent> messageConsumer) {
        System.out.println("line:"+line);
        return false;
    }
}
