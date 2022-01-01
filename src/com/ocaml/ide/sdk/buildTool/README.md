# How does it work?

*(From my point of view, more to come)*

## OCamlBuildTaskRunner 

**EP**: projectTaskRunner

The runner will handle the "Make project" (a.k.a. Build) instead of the IDE, if the task is inside an OCaml Module.

* canRun: check if we can call run
* run: trigger the build
    * We are exploring "every" tasks -> expandTask
    * We are executing our tasks -> executeTask
      * See `build`, creating the "context" (that will be used to store everything related to the execution such as the duration, the number of warnings, etc.)
      * See `execute`, that is starting the process with `waitAndStart` then `doExecute` (in most cases)
      * If one task failed, the build is canceled

We are attaching a processAdapter to the process, allowing us to fire the events (notify...). The BuildContentDescriptor are the small horizontal tabs in build.

## Show the build menu

The lines showing the Build menu are
```java
class Anonymous extends OCamlcBuildContext {
    @Override public void doExecute() {
        // ...
        var buildToolWindow = BuildContentManager.getInstance(project).getOrCreateToolWindow();
        buildToolWindow.setAvailable(true, null);
        buildToolWindow.activate(null);
        // ...
    }
}
```