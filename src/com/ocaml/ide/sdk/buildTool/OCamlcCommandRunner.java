package com.ocaml.ide.sdk.buildTool;

import com.intellij.execution.*;
import com.intellij.execution.configurations.*;
import com.intellij.execution.executors.*;
import com.intellij.execution.runners.*;
import com.intellij.execution.ui.*;
import com.ocaml.ide.sdk.runConfiguration.*;
import org.jetbrains.annotations.*;
import org.jetbrains.concurrency.*;

public class OCamlcCommandRunner implements ProgramRunner<RunnerSettings> {
    public static final String RUNNER_ID = "OCamlcCommandRunner";

    @Override public @NotNull @NonNls String getRunnerId() {
        return RUNNER_ID;
    }

    @Override public boolean canRun(@NotNull String executorId, @NotNull RunProfile profile) {
        return executorId.equals(DefaultRunExecutor.EXECUTOR_ID) &&
                profile instanceof OCamlcRunConfiguration;
    }

    @Override public void execute(@NotNull ExecutionEnvironment environment) throws ExecutionException {
        var state = environment.getState();
        if (state == null) return;
        //noinspection UnstableApiUsage
        ExecutionManager
                .getInstance(environment.getProject())
                .startRunProfile(environment, () -> Promises.resolvedPromise(doExecute(state, environment)));
    }

    protected RunContentDescriptor doExecute(RunProfileState state, ExecutionEnvironment environment) {
        return DefaultProgramRunnerKt.executeState(state, environment, this);
    }
}
