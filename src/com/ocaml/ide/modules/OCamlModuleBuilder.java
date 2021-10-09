package com.ocaml.ide.modules;

import com.intellij.execution.impl.*;
import com.intellij.ide.util.projectWizard.*;
import com.intellij.openapi.*;
import com.intellij.openapi.module.*;
import com.intellij.openapi.options.*;
import com.intellij.openapi.project.*;
import com.intellij.openapi.projectRoots.*;
import com.intellij.openapi.roots.*;
import com.intellij.openapi.util.io.*;
import com.intellij.openapi.vfs.*;
import com.ocaml.ide.sdk.*;
import com.ocaml.utils.*;
import icons.*;
import org.jetbrains.annotations.*;

import javax.swing.*;
import java.io.*;
import java.util.*;

public class OCamlModuleBuilder extends ModuleBuilder {
    private static final String OCAML_GROUP_NAME = "OCaml Language";
    private static final Log LOG = Log.create("module.builder");

    private final String myBuilderId;
    private final String myPresentableName;
    private final String myDescription;

    public OCamlModuleBuilder() {
        myBuilderId = OCamlModuleType.OCAML_MODULE_ID;
        myPresentableName = OCamlModuleType.OCAML_MODULE_NAME;
        myDescription = OCamlModuleType.OCAML_MODULE_DESCRIPTION;
    }

    @Override
    public String getBuilderId() {
        return myBuilderId;
    }

    @Override
    public Icon getNodeIcon() {
        return ORIcons.OCL_MODULE;
    }

    @Override
    public String getDescription() {
        return myDescription;
    }

    @Override
    public String getPresentableName() {
        return myPresentableName;
    }

    @Override
    public String getGroupName() {
        return OCAML_GROUP_NAME;
    }

    @Override
    public String getParentGroup() {
        return OCAML_GROUP_NAME;
    }

    @Override
    public void setupRootModel(@NotNull final ModifiableRootModel rootModel) {
        if (myJdk != null) {
            rootModel.setSdk(myJdk);
        } else {
            rootModel.inheritSdk();
        }

        final ContentEntry contentEntry = doAddContentEntry(rootModel);
        if (contentEntry != null) {
            for (final String sourcePath : getSourcePaths()) {
                final VirtualFile sourceRoot = LocalFileSystem.getInstance()
                        .refreshAndFindFileByPath(FileUtil.toSystemIndependentName(sourcePath));

                if (sourceRoot != null) {
                    contentEntry.addSourceFolder(sourceRoot, false);
                }
            }
        }
    }

    @NotNull
    public List<String> getSourcePaths() {
        @NonNls final String path = getContentEntryPath() + File.separator + "src";

        if(new File(path).mkdirs()) LOG.info("Create src folder: " + path);
        else LOG.warn("Failed to create src folder: " + path);

        return Collections.singletonList(path);
    }

    @Override
    public boolean isSuitableSdkType(final SdkTypeId sdkType) {
        return sdkType == OCamlSdkType.getInstance();
    }

    @Override
    public ModuleType<OCamlModuleBuilder> getModuleType() {
        return OCamlModuleType.getInstance();
    }

    @Nullable @Override public ModuleWizardStep getCustomOptionsStep(WizardContext context, Disposable parentDisposable) {
        return new OCamlCompilerWizardStep(context, this);
    }
}