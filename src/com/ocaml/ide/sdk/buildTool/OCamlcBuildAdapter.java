package com.ocaml.ide.sdk.buildTool;

import com.intellij.build.*;
import com.intellij.build.events.*;
import com.intellij.build.events.impl.*;
import com.intellij.build.output.*;
import com.intellij.execution.actions.*;
import com.intellij.execution.process.*;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.util.*;
import com.intellij.util.*;
import org.jetbrains.annotations.*;

import javax.swing.*;
import java.util.*;

public class OCamlcBuildAdapter extends ProcessAdapter {
    private final OCamlcBuildContext myContext;
    private final BuildProgressListener myBuildProgressListener;
    private final BuildOutputInstantReaderImpl myInstantReader;

    public OCamlcBuildAdapter(OCamlcBuildContext context,
                              BuildProgressListener buildProgressListener) {
        myContext = context;
        myBuildProgressListener = buildProgressListener;
        myInstantReader = new BuildOutputInstantReaderImpl(
                myContext.myBuildId,
                myContext.myBuildId,
                myBuildProgressListener,
                Collections.singletonList(new OCamlcBuildEventsConverter(context))
        );

        context.notifyProcessStarted(context.myProcessHandler);

        var buildContentDescriptor = new BuildContentDescriptor(
                null,
                null, new JComponent(){},
                "Build"
        );
        buildContentDescriptor.setActivateToolWindowWhenAdded(true);
        buildContentDescriptor.setActivateToolWindowWhenFailed(true);
        buildContentDescriptor.setNavigateToError(ThreeState.fromBoolean(true));

        var descriptor = new DefaultBuildDescriptor(
                myContext.myBuildId,
                "Run OCamlc Command",
                context.myConfiguration.getWorkingDirectory(),
                context.myStarted
        );
        descriptor.withContentDescriptor(() -> buildContentDescriptor);
        // todo: add back rerun when the run is properly calling build
        //noinspection UnstableApiUsage
        //descriptor.withRestartAction(createRerunAction(myContext.myProcessHandler, context.myEnvironment));
        //noinspection UnstableApiUsage
        descriptor.withRestartAction(createStopAction(myContext.myProcessHandler)); // stop

        var buildStarted = new StartBuildEventImpl(descriptor, "Build running...");
        buildProgressListener.onEvent(myContext.myBuildId, buildStarted);
    }

    private AnAction createStopAction(ProcessHandler processHandler) {
        return new StopProcessAction("Stop", "Stop", processHandler);
    }

    //private static AnAction createRerunAction(ProcessHandler processHandler, ExecutionEnvironment environment) {
    //    return new DumbAwareAction() {
    //        private final Project myProject = environment.getProject();
    //        private final RunnerAndConfigurationSettings mySettings = environment.getRunnerAndConfigurationSettings();
    //        private final String text = "Rerun '"+StringUtil.escapeMnemonics(environment.getRunProfile().getName())+"'";
    //
    //        @Override public void update(@NotNull AnActionEvent event) {
    //            super.update(event);
    //            var presentation = event.getPresentation();
    //            presentation.setText(text, false);
    //            presentation.setIcon(processHandler.isProcessTerminated() ?
    //                    AllIcons.Actions.Compile :
    //                    AllIcons.Actions.Restart);
    //            presentation.setEnabled(isEnabled());
    //        }
    //
    //        private boolean isEnabled() {
    //            return (!DumbService.isDumb(myProject)
    //                    || mySettings == null || mySettings.getType().isDumbAware())
    //                    && !ExecutorRegistry.getInstance().isStarting(environment) &&
    //                    !processHandler.isProcessTerminating();
    //        }
    //
    //        @Override public void actionPerformed(@NotNull AnActionEvent e) {
    //            ExecutionManagerImpl.stopProcess(processHandler);
    //            ExecutionUtil.restart(environment);
    //        }
    //    };
    //}

    @Override public void processTerminated(@NotNull ProcessEvent event) {
        myInstantReader.closeAndGetFuture().whenComplete((unit, error) -> {
            boolean isSuccess = event.getExitCode() == 0 && myContext.myErrors.get() == 0;
            boolean isCanceled = myContext.isCanceled();
            onBuildOutputReaderFinish(event, isSuccess, isCanceled, error);
        });
    }

    @Override public void processWillTerminate(@NotNull ProcessEvent event, boolean willBeDestroyed) {
        myContext.notifyProcessTerminating(event.getProcessHandler());
    }

    @Override public void onTextAvailable(@NotNull ProcessEvent event, @NotNull Key outputType) {
        myInstantReader.append(event.getText());
    }

    private void onBuildOutputReaderFinish(ProcessEvent event, boolean isSuccess,
                                           boolean isCanceled, Throwable error) {
        String status;
        EventResult result;

        if (isCanceled) {
            status = "canceled";
            result = new SkippedResultImpl();
        } else if (isSuccess) {
            status = "successful";
            result = new SuccessResultImpl();
        } else {
            status = "failed";
            result = new FailureResultImpl(error);
        }

        var buildFinished = new FinishBuildEventImpl(
                myContext.myBuildId,
                null,
                System.currentTimeMillis(),
                "Build "+status,
                result
        );
        myBuildProgressListener.onEvent(myContext.myBuildId, buildFinished);
        myContext.finished(isSuccess);
        myContext.notifyProcessTerminated(event.getProcessHandler(), event.getExitCode());
    }
}