package com.ocaml.ide.sdk.buildTool.tasks;

import com.intellij.openapi.progress.*;
import com.intellij.openapi.project.*;
import com.intellij.task.*;
import com.ocaml.ide.sdk.buildTool.*;
import org.jetbrains.annotations.*;
import org.jetbrains.concurrency.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

@SuppressWarnings("UnstableApiUsage")
public class BackgroundableProjectTaskRunner extends Task.Backgroundable {
    private final ProjectTask[] myTasks;
    private final OCamlBuildTaskRunner myParentRunner;
    private final AsyncPromise<ProjectTaskRunner.Result> myTotalPromise;
    private final CompletableFuture<ProgressIndicator> myWaitingIndicator;
    public final CompletableFuture<Boolean> myExecutionStarted;

    public BackgroundableProjectTaskRunner(@NotNull Project project,
                                           ProjectTask[] tasks,
                                           OCamlBuildTaskRunner parentRunner,
                                           AsyncPromise<ProjectTaskRunner.Result> totalPromise,
                                           CompletableFuture<ProgressIndicator> waitingIndicator) {
        super(project, "Building...", true);
        myTasks = tasks;
        myParentRunner = parentRunner;
        myTotalPromise = totalPromise;
        myWaitingIndicator = waitingIndicator;
        myExecutionStarted = new CompletableFuture<>();
    }

    @Override public void run(@NotNull ProgressIndicator indicator) {
        if (!waitForStart()) {
            if (myTotalPromise.getState() == Promise.State.PENDING) {
                myTotalPromise.cancel();
            }
            return;
        }

        // collect tasks
        var expandedTasks = Arrays.stream(myTasks)
                .filter(myParentRunner::canRun)
                .map(myParentRunner::expandTask);
        Collection<ProjectTask> allTasks = expandedTasks
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        // nothing to do
        if (allTasks.isEmpty()) {
            myTotalPromise.setResult(TaskRunnerResults.FAILURE);
            return;
        }

        try {
            for (ProjectTask task : allTasks) {
                var promise = myParentRunner.executeTask(task);
                if (promise.blockingGet(Integer.MAX_VALUE) != TaskRunnerResults.SUCCESS) {
                    // Do not continue session if one of builds failed
                    myTotalPromise.setResult(TaskRunnerResults.FAILURE);
                    break;
                }
            }

            // everything succeeded - set final result to success
            if (myTotalPromise.getState() == Promise.State.PENDING) {
                myTotalPromise.setResult(TaskRunnerResults.SUCCESS);
            }
        } catch (CancellationException e) {
            myTotalPromise.setResult(TaskRunnerResults.ABORTED);
            throw new ProcessCanceledException(e);
        } catch (Throwable e) {
            myTotalPromise.setResult(TaskRunnerResults.FAILURE);
        }
    }

    private boolean waitForStart() {
        try {
            // Check if this build wasn't cancelled while it was in queue through waiting indicator
            var cancelled = myWaitingIndicator.get().isCanceled();
            // Notify waiting background task that this build started and there is no more need for this indicator
            myExecutionStarted.complete(true);
            return !cancelled;
        } catch (InterruptedException | CancellationException e) {
            myTotalPromise.setResult(TaskRunnerResults.ABORTED);
            throw new ProcessCanceledException(e);
        } catch (Throwable e) {
            myTotalPromise.setResult(TaskRunnerResults.FAILURE);
            throw new ProcessCanceledException(e);
        }
    }
}
