package com.ocaml.ide.sdk.buildTool.tasks;

import com.intellij.openapi.progress.*;
import com.intellij.openapi.project.*;
import org.jetbrains.annotations.*;

import java.util.concurrent.*;

public class WaitingTask extends Task.Backgroundable {
    private final CompletableFuture<ProgressIndicator> myWaitingIndicator;
    private final CompletableFuture<Boolean> myExecutionStarted;

    public WaitingTask(
            @NotNull Project project,
            CompletableFuture<ProgressIndicator> waitingIndicator,
            CompletableFuture<Boolean> executionStarted) {
        super(project, "Waiting for the current build to finish...", true);
        myWaitingIndicator = waitingIndicator;
        myExecutionStarted = executionStarted;
    }

    @Override public void run(@NotNull ProgressIndicator indicator) {
        // Wait until queued task will start executing.
        // Needed so that user can cancel build tasks from queue.
        myWaitingIndicator.complete(indicator);
        try {
            while (true) {
                indicator.checkCanceled();
                try {
                    myExecutionStarted.get(10, TimeUnit.MILLISECONDS);
                    break;
                } catch (TimeoutException ignore) {}
            }
        } catch (CancellationException | InterruptedException | ExecutionException e) {
            throw new ProcessCanceledException(e);
        }
    }
}
