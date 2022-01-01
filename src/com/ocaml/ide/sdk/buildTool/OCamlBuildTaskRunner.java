package com.ocaml.ide.sdk.buildTool;

import com.intellij.build.*;
import com.intellij.execution.*;
import com.intellij.execution.executors.*;
import com.intellij.execution.impl.*;
import com.intellij.execution.process.*;
import com.intellij.execution.runners.*;
import com.intellij.openapi.application.*;
import com.intellij.openapi.fileEditor.*;
import com.intellij.openapi.module.*;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.progress.*;
import com.intellij.openapi.project.*;
import com.intellij.task.*;
import com.intellij.task.impl.*;
import com.intellij.util.ui.*;
import com.ocaml.ide.modules.*;
import com.ocaml.ide.sdk.buildTool.tasks.*;
import com.ocaml.ide.sdk.runConfiguration.*;
import org.jetbrains.annotations.*;
import org.jetbrains.concurrency.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.ExecutionException;

public class OCamlBuildTaskRunner extends ProjectTaskRunner {

    @Override public boolean canRun(@NotNull ProjectTask projectTask) {
        if (projectTask instanceof ModuleBuildTask) {
            Module module = ((ModuleBuildTask) projectTask).getModule();
            return ModuleType.get(module).equals(OCamlModuleType.getInstance());
        } /*todo: ukn else if (projectTask instanceof ProjectModelBuildTask) {
            ProjectModelBuildTask<?> buildTask = ((ProjectModelBuildTask<?>) projectTask);
            ProjectModelBuildableElement buildableElement = buildTask.getBuildableElement();
            return buildableElement instanceof OCamlcRunConfiguration;
        }*/
        return false;
    }

    @Override public Promise<Result> run(@NotNull Project project, @NotNull ProjectTaskContext context, ProjectTask @NotNull ... tasks) {
        if (project.isDisposed()) return Promises.rejectedPromise("Project disposed");

        var resultPromise = new AsyncPromise<Result>();
        var waitingIndicator = new CompletableFuture<ProgressIndicator>();
        var queuedTask = new BackgroundableProjectTaskRunner(
                project,
                tasks,
                this,
                resultPromise,
                waitingIndicator
        );

        WaitingTask w = new WaitingTask(project, waitingIndicator, queuedTask.myExecutionStarted);
        w.queue();

        OCamlcBuildSessionsQueueManager.getInstance(project)
                .buildSessionsQueue
                .run(queuedTask, ModalityState.defaultModalityState(), new EmptyProgressIndicator());

        return resultPromise;
    }

    /**
     * This method was cut. It is creating lists
     * of one element of type ProjectModelBuildTask&lt;OCamlBuildConfiguration&gt;
     * @see #executeTask(ProjectTask)
     */
    public List<ProjectTask> expandTask(ProjectTask task) {
        if (!(task instanceof ModuleBuildTask)) return Collections.singletonList(task);

        var project = ((ModuleBuildTask) task).getModule().getProject();
        var runManager = RunManager.getInstance(project);

        // get selected configuration
        var selectedConfiguration = runManager.getSelectedConfiguration();
        var configuration = selectedConfiguration == null ? null : selectedConfiguration.getConfiguration();
        if (!(configuration instanceof OCamlcRunConfiguration)) return Collections.emptyList();

        OCamlcRunConfiguration buildConfiguration = (OCamlcRunConfiguration) configuration;
        var environment = createBuildEnvironment(buildConfiguration);
        var buildableElement = new OCamlBuildConfiguration(buildConfiguration, environment);

        return Collections.singletonList(new ProjectModelBuildTaskImpl<>(buildableElement, false));
    }

    public ExecutionEnvironment createBuildEnvironment(OCamlcRunConfiguration buildConfiguration) {
        var project = buildConfiguration.getProject();

        RunManager runManager = RunManager.getInstance(project);
        if (!(runManager instanceof RunManagerImpl)) return null;

        var executor = ExecutorRegistry.getInstance().getExecutorById(DefaultRunExecutor.EXECUTOR_ID);
        if (executor == null) return null;

        var runner = ProgramRunner.findRunnerById(OCamlcCommandRunner.RUNNER_ID);
        if (runner == null) return null;

        var settings = new RunnerAndConfigurationSettingsImpl((RunManagerImpl) runManager, buildConfiguration);
        settings.setActivateToolWindowBeforeRun(true);

        return new ExecutionEnvironment(executor, runner, settings, project);
    }

    /**
     * Actually "build" the project using the configuration
     * loaded in expandTask
     */
    @SuppressWarnings("UnstableApiUsage")
    public Promise<Result> executeTask(ProjectTask task) {
        if (!(task instanceof ProjectModelBuildTask))
            return Promises.resolvedPromise(TaskRunnerResults.ABORTED);

        var buildableElement = ((OCamlBuildConfiguration) ((ProjectModelBuildTask<?>) task).getBuildableElement());

        ProjectTaskRunner.Result result;

        try {
            // BUILD
            var buildFuture = build(buildableElement);
            var buildResult = buildFuture.get();
            // explorer result
            if (buildResult.canceled) result = TaskRunnerResults.ABORTED;
            else if (buildResult.succeeded) result = TaskRunnerResults.SUCCESS;
            else result = TaskRunnerResults.FAILURE;
        } catch (Exception e) {
            result = TaskRunnerResults.FAILURE;
        }

        var promise = new AsyncPromise<Result>();
        promise.setResult(result);
        return promise;
    }

    @NotNull
    private Future<OCamlcBuildResult> build(OCamlBuildConfiguration buildConfiguration) {
        var configuration = buildConfiguration.myConfiguration;
        var environment = buildConfiguration.myEnvironment;
        var project = environment.getProject();

        return execute(
                new OCamlcBuildContext(configuration, project, environment) {
                    @Override public void doExecute() {
                        BuildViewManager buildProgressListener = project.getService(BuildViewManager.class);

                        // make buildTool Window available
                        var buildToolWindow = BuildContentManager.getInstance(project).getOrCreateToolWindow();
                        buildToolWindow.setAvailable(true, null);
                        buildToolWindow.activate(null);

                        try {
                            myProcessHandler = getState().startProcess();
                        } catch (com.intellij.execution.ExecutionException e) {
                            System.out.println("start failed");
                        }
                        if (myProcessHandler != null) {
                            myProcessHandler.addProcessListener(
                                    new OCamlcBuildAdapter(this, buildProgressListener));
                            myProcessHandler.startNotify();
                        }
                    }
                }
        );
    }

    private CompletableFuture<OCamlcBuildResult> execute(OCamlcBuildContext context) {
        context.notifyProcessStartScheduled();
        var processCreationLock = new Object();

        var indicatorResult = new CompletableFuture<ProgressIndicator>();
        UIUtil.invokeLaterIfNeeded(() -> new Task.Backgroundable(context.myProject, "Build", true) {
            @Override public void run(@NotNull ProgressIndicator indicator) {
                indicatorResult.complete(indicator);

                var wasCanceled = false;
                while (!context.myResult.isDone()) {
                    if (!wasCanceled && indicator.isCanceled()) {
                        wasCanceled = true;
                        synchronized(processCreationLock) {
                            ProcessHandler processHandler = context.myProcessHandler;
                            if (processHandler != null) processHandler.destroyProcess();
                        }
                    }

                    try {
                        //noinspection BusyWait
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new ProcessCanceledException(e);
                    }
                }
            }
        }.queue());

        try {
            context.myIndicator = indicatorResult.get();
        } catch (ExecutionException | InterruptedException e) {
            context.myResult.completeExceptionally(e);
            return context.myResult;
        }

        if (context.myIndicator != null) {
            context.myIndicator.setText("Building...");
            context.myIndicator.setText2("");
        }

        ApplicationManager.getApplication().executeOnPooledThread(
                () -> {
                    if (!context.waitAndStart()) return;
                    context.notifyProcessStarting();

                    ApplicationManager.getApplication().invokeLater(() -> {
                        synchronized(processCreationLock) {
                            var isCanceled = context.isCanceled();
                            if (isCanceled) {
                                context.canceled();
                                return;
                            }

                            FileDocumentManager.getInstance().saveAllDocuments();
                            context.doExecute();
                        }
                    });

                }
        );
        return context.myResult;
    }
}
