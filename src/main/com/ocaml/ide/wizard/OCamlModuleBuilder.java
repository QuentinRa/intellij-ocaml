package com.ocaml.ide.wizard;

import com.intellij.ide.util.projectWizard.ModuleBuilder;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.SettingsStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.projectRoots.SdkTypeId;
import com.intellij.openapi.roots.CompilerModuleExtension;
import com.intellij.openapi.roots.ContentEntry;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ui.configuration.ModulesProvider;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.platform.ProjectTemplate;
import com.ocaml.ide.module.OCamlModuleType;
import com.ocaml.ide.sdk.OCamlSdkType;
import com.ocaml.ide.wizard.templates.OCamlTemplateProvider;
import com.ocaml.ide.wizard.templates.TemplateBuildInstructions;
import com.ocaml.ide.wizard.view.OCamlSdkWizardStep;
import com.ocaml.ide.wizard.view.OCamlSelectTemplate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 * Set up an OCaml module/project.
 *
 * <br><br>---- getWeight ----<br>
 * You may use this to move this module
 * to the top in the list, if needed (ex: 100).
 *
 * <br><br>---- getGroupName / getParentGroup ----<br>
 * You may use this if this module is inside a group.
 */
public class OCamlModuleBuilder extends ModuleBuilder {

    private ProjectTemplate myTemplate;

    @Override
    public ModuleType<OCamlModuleBuilder> getModuleType() {
        return new OCamlModuleType();
    }

    @Override
    public boolean isSuitableSdkType(SdkTypeId sdkType) {
        return sdkType instanceof OCamlSdkType;
    }

    public void setProjectTemplate(ProjectTemplate template) {
        myTemplate = template;
    }

    @Override
    public void setupRootModel(@NotNull ModifiableRootModel rootModel) {
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

    /**
     * the options' step is the first step, the user will select an SDK
     **/
    @Nullable
    @Override
    public ModuleWizardStep getCustomOptionsStep(WizardContext context, Disposable parentDisposable) {
        return new OCamlSdkWizardStep(context, this);
    }

    /**
     * Show steps after the custom option steps
     * and before the project step
     */
    @Override
    public ModuleWizardStep[] createWizardSteps(@NotNull WizardContext wizardContext, @NotNull ModulesProvider modulesProvider) {
        return new ModuleWizardStep[]{new OCamlSelectTemplate(wizardContext, OCamlTemplateProvider.getAvailableTemplates())};
    }

    /**
     * The default one is enough for us
     */
    @Override
    public ModuleWizardStep modifyProjectTypeStep(@NotNull SettingsStep settingsStep) {
        return super.modifyProjectTypeStep(settingsStep);
    }
}