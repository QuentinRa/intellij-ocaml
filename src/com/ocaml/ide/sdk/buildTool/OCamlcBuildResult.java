package com.ocaml.ide.sdk.buildTool;

public class OCamlcBuildResult {
    public final boolean succeeded;
    public final boolean canceled;
    public final Long started;
    public final Long duration;
    public final int errors;
    public final int warnings;
    public final String message;

    public OCamlcBuildResult(boolean succeeded,
                             boolean canceled,
                             Long started,
                             Long duration,
                             int errors,
                             int warnings,
                             String message) {
        this.succeeded = succeeded;
        this.canceled = canceled;
        this.started = started;
        this.duration = duration;
        this.errors = errors;
        this.warnings = warnings;
        this.message = message;
    }
}
