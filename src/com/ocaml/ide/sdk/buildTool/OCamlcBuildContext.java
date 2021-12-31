package com.ocaml.ide.sdk.buildTool;

import com.intellij.build.*;
import com.intellij.execution.*;
import com.intellij.execution.process.*;
import com.intellij.execution.runners.*;
import com.intellij.ide.nls.*;
import com.intellij.notification.*;
import com.intellij.openapi.application.*;
import com.intellij.openapi.progress.*;
import com.intellij.openapi.project.*;
import com.intellij.openapi.ui.*;
import com.intellij.openapi.util.*;
import com.intellij.openapi.wm.*;
import com.intellij.ui.*;
import com.ocaml.ide.sdk.runConfiguration.*;
import com.ocaml.utils.*;


import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public abstract class OCamlcBuildContext {

    private static final Key<Semaphore> BUILD_SEMAPHORE_KEY = Key.create("BUILD_SEMAPHORE_KEY");

    public final AtomicInteger myWarnings;
    public final AtomicInteger myErrors;

    public final OCamlcRunConfiguration myConfiguration;
    public final Project myProject;
    public final ExecutionEnvironment myEnvironment;
    public final Object myBuildId;

    public volatile ProgressIndicator myIndicator;
    public volatile ProcessHandler myProcessHandler;

    private final Semaphore myBuildSemaphore;
    public final CompletableFuture<OCamlcBuildResult> myResult;

    public Long myStarted;
    private volatile Long myFinished;

    public OCamlcBuildContext(OCamlcRunConfiguration configuration,
                              Project project,
                              ExecutionEnvironment environment) {
        myBuildId = new Object();
        myConfiguration = configuration;
        myProject = project;
        myEnvironment = environment;
        myIndicator = null;
        myErrors = new AtomicInteger();
        myWarnings = new AtomicInteger();

        Semaphore userData = project.getUserData(BUILD_SEMAPHORE_KEY);
        myBuildSemaphore = userData == null ?
                ((UserDataHolderEx)project).putUserDataIfAbsent(BUILD_SEMAPHORE_KEY, new Semaphore(1))
                : userData;

        myResult = new CompletableFuture<>();

        myStarted = System.currentTimeMillis();
        myFinished = myStarted;
    }

    public abstract void doExecute();

    public Long getDuration() { return myFinished - myStarted; }
    public ExecutionListener getExecutionListener() { return myProject.getMessageBus().syncPublisher(ExecutionManager.EXECUTION_TOPIC); }

    // other

    public boolean waitAndStart() {
        if (myIndicator != null) myIndicator.pushState();
        try {
            if (myIndicator != null) {
                myIndicator.setText("Waiting for the current build to finish...");
                myIndicator.setText2("");
            }
            while (true) {
                if (myIndicator != null) myIndicator.checkCanceled();
                try {
                    if (myBuildSemaphore.tryAcquire(100, TimeUnit.MILLISECONDS)) break;
                } catch (InterruptedException ignored) {
                    throw new ProcessCanceledException();
                }
            }
        } catch (ProcessCanceledException e) {
            canceled();
            return false;
        } finally {
            if (myIndicator != null) myIndicator.popState();
        }
        return true;
    }

    public void finished(boolean isSuccess) {
        var isCanceled = isCanceled();

        myFinished = System.currentTimeMillis();
        myBuildSemaphore.release();

        String finishMessage, finishDetails;
        MessageType messageType;
        int errors = myErrors.get(), warnings = myWarnings.get();

        // We report successful builds with errors or warnings correspondingly
        if (isCanceled) {
            finishMessage = "Build canceled";
            finishDetails = null;
            messageType = MessageType.INFO;
        } else {
            var hasWarningsOrErrors = errors > 0 || warnings > 0;
            finishMessage = isSuccess ? "Build finished" : "Build failed";
            if (hasWarningsOrErrors) {
                var errorsString =  errors == 1 ? "error" : "errors";
                var warningsString = warnings == 1 ? "warning" : "warnings";
                finishDetails = errors + " " + errorsString + " and " + warnings + " "+warningsString;
            } else {
                finishDetails = null;
            }

            if (!isSuccess) messageType = MessageType.ERROR;
            else if (hasWarningsOrErrors) messageType = MessageType.WARNING;
            else messageType = MessageType.INFO;
        }

        myResult.complete(new OCamlcBuildResult(
                isSuccess,
                isCanceled,
                myStarted,
                getDuration(),
                errors,
                warnings,
                finishMessage
        ));

        showBuildNotification(messageType, finishMessage, finishDetails, getDuration());
    }

    private void showBuildNotification(MessageType messageType,
                                       String message,
                                       String details,
                                       Long time) {
        var notificationContent = message + (details == null ? "" : " with " + details);
        if (time > 0) notificationContent += " in " + NlsMessages.formatDurationApproximateNarrow(time);

        var notificationData = new ORNotifications.OCamlNotificationData(notificationContent);
        notificationData.myMessageType = messageType;
        Notification notification = ORNotifications.notifyBuild(notificationData);

        if (messageType == MessageType.ERROR) {
            var manager = ToolWindowManager.getInstance(myProject);
            String finalNotificationContent = notificationContent;
            ApplicationManager.getApplication().invokeLater(() -> {
                manager.notifyByBalloon(
                        BuildContentManager.TOOL_WINDOW_ID,
                        messageType,
                        finalNotificationContent
                );
            });
        }
        SystemNotifications.getInstance().notify(
                notification.getGroupId(),
                // capitalize
                message.substring(0, 1).toUpperCase() + message.substring(1),
                details == null ? "" : details
        );
    }

    public void canceled() {
        myFinished = System.currentTimeMillis();

        myResult.complete(new OCamlcBuildResult(
                false,
                true,
                myStarted,
                getDuration(),
                myErrors.get(),
                myWarnings.get(),
                "Build canceled"
        ));

        notifyProcessNotStarted();
    }

    public void notifyProcessStartScheduled() {
        ExecutionListener executionListener = getExecutionListener();
        executionListener.processStartScheduled(myEnvironment.getExecutor().getId(), myEnvironment);
    }

    public void notifyProcessNotStarted() {
        ExecutionListener executionListener = getExecutionListener();
        executionListener.processNotStarted(myEnvironment.getExecutor().getId(), myEnvironment);
    }

    public void notifyProcessStarting() {
        ExecutionListener executionListener = getExecutionListener();
        executionListener.processStarting(myEnvironment.getExecutor().getId(), myEnvironment);
    }

    public void notifyProcessStarted(ProcessHandler handler) {
        ExecutionListener executionListener = getExecutionListener();
        executionListener.processStarted(myEnvironment.getExecutor().getId(), myEnvironment, handler);
    }

    public void notifyProcessTerminating(ProcessHandler handler) {
        ExecutionListener executionListener = getExecutionListener();
        executionListener.processTerminating(myEnvironment.getExecutor().getId(), myEnvironment, handler);
    }

    public void notifyProcessTerminated(ProcessHandler handler, int exitCode) {
        ExecutionListener executionListener = getExecutionListener();
        executionListener.processTerminated(myEnvironment.getExecutor().getId(), myEnvironment, handler, exitCode);
    }

    public OCamlcBuildState getState() {
        return new OCamlcBuildState(this);
    }

    public boolean isCanceled() {
        return myIndicator != null && myIndicator.isCanceled();
    }
}
