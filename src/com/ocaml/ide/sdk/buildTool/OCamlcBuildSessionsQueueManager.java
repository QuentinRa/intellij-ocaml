package com.ocaml.ide.sdk.buildTool;

import com.intellij.openapi.components.*;
import com.intellij.openapi.progress.*;
import com.intellij.openapi.project.*;

@Service
public final class OCamlcBuildSessionsQueueManager {
    public final BackgroundTaskQueue buildSessionsQueue;

    public OCamlcBuildSessionsQueueManager(Project project) {
        buildSessionsQueue = new BackgroundTaskQueue(project, "Building...");
    }

    public static OCamlcBuildSessionsQueueManager getInstance(Project project) {
        return project.getService(OCamlcBuildSessionsQueueManager.class);
    }
}
