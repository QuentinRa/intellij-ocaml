package com.ocaml.ide.wizard;

import com.intellij.ide.util.projectWizard.*;
import com.intellij.openapi.*;
import com.intellij.openapi.application.*;
import com.intellij.openapi.module.*;
import com.intellij.openapi.projectRoots.*;
import com.intellij.openapi.roots.*;
import com.intellij.openapi.roots.ui.configuration.*;
import com.intellij.openapi.util.io.*;
import com.intellij.openapi.vfs.*;
import com.intellij.platform.*;
import com.ocaml.ide.module.*;
import com.ocaml.ide.wizard.templates.*;
import com.ocaml.ide.wizard.view.*;
import com.ocaml.ide.sdk.*;
import org.jetbrains.annotations.*;

import java.io.*;

/**
 * Set up an OCaml module/project.
 *
 * ---- getWeight
 * You may use this to move this module
 * to the top in the list, if needed (ex: 100).
 *
 * -- getGroupName / getParentGroup
 * You may use this if this module is inside a group.
 */
public class OCamlModuleBuilder extends ModuleBuilder {

    private ProjectTemplate myTemplate;

    @Override public ModuleType<OCamlModuleBuilder> getModuleType() {
        return new OCamlModuleType();
    }

    @Override public boolean isSuitableSdkType(SdkTypeId sdkType) {
        return sdkType instanceof OCamlSdkType;
    }

    public void setProjectTemplate(ProjectTemplate template) {
        myTemplate = template;
    }

    @Override public void setupRootModel(@NotNull ModifiableRootModel rootModel) {
        // output folder
        final CompilerModuleExtension compilerModuleExtension = rootModel.getModuleExtension(CompilerModuleExtension.class);
        compilerModuleExtension.setExcludeOutput(true);
        compilerModuleExtension.inheritCompilerOutputPath(true);

        // set the SDK "JDK"
        if (myJdk != null) rootModel.setSdk(myJdk);
        else rootModel.inheritSdk();

        // Create the files/folders
        ContentEntry contentEntry = doAddContentEntry(rootModel);
        if (contentEntry != null) {
            // Get instructions
            TemplateBuildInstructions instructions;
            if (myTemplate instanceof TemplateBuildInstructions)
                instructions = (TemplateBuildInstructions) myTemplate;
            else instructions = OCamlTemplateProvider.getDefaultInstructions();

            // create the source folder
            final String sourcePath = getContentEntryPath() + File.separator + instructions.getSourceFolderName();
            //noinspection ResultOfMethodCallIgnored
            new File(sourcePath).mkdirs();
            final VirtualFile sourceRoot = LocalFileSystem.getInstance()
                    .refreshAndFindFileByPath(FileUtil.toSystemIndependentName(sourcePath));
            if (sourceRoot != null) {
                contentEntry.addSourceFolder(sourceRoot, false, "");
                // create the files
                instructions.createFiles(rootModel, sourceRoot);
                // refresh
                ApplicationManager.getApplication().runWriteAction(() -> sourceRoot.refresh(true, true));
            }
        }
    }

    /** the options' step is the first step, the user will select an SDK **/
    @Nullable @Override public ModuleWizardStep getCustomOptionsStep(WizardContext context, Disposable parentDisposable) {
        return new OCamlSdkWizardStep(context, this);
    }

    /**
     * Show steps after the custom option steps
     * and before the project step
     */
    @Override public ModuleWizardStep[] createWizardSteps(@NotNull WizardContext wizardContext, @NotNull ModulesProvider modulesProvider) {
        return new ModuleWizardStep[] { new OCamlSelectTemplate(wizardContext, OCamlTemplateProvider.getAvailableTemplates()) };
    }

    /**
     * The default one is enough for us
     */
    @Override public ModuleWizardStep modifyProjectTypeStep(@NotNull SettingsStep settingsStep) {
        return super.modifyProjectTypeStep(settingsStep);
    }
}