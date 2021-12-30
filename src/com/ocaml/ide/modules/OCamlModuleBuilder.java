package com.ocaml.ide.modules;

import com.intellij.execution.wsl.*;
import com.intellij.ide.util.projectWizard.*;
import com.intellij.openapi.*;
import com.intellij.openapi.module.*;
import com.intellij.openapi.project.*;
import com.intellij.openapi.projectRoots.*;
import com.intellij.openapi.roots.*;
import com.intellij.openapi.roots.libraries.*;
import com.intellij.openapi.util.io.*;
import com.intellij.openapi.vfs.*;
import com.ocaml.ide.sdk.*;
import com.ocaml.ide.sdk.library.*;
import com.ocaml.ide.sdk.sources.*;
import com.ocaml.ide.settings.*;
import com.ocaml.utils.*;
import icons.*;
import org.jetbrains.annotations.*;

import javax.swing.*;
import java.io.*;
import java.util.*;

/**
 * todo: do not ask for the JDK again if multiples modules
 *  (this solves the problem of having only one value in the settings)
 */
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

        if (myJdk != null && myJdk.getHomePath() != null) {
            // the JDK Name is OCaml-version (ex: OCaml-4.05.0)
            // but, we know that the switchName is 4.05.0 (=the version)
            // so there is no need to match the version in the name
            // we are fetching the version directly
            String switchName = myJdk.getVersionString();
            String homePath = myJdk.getHomePath();

            // create
            LibraryTable.ModifiableModel projectLibraryModel = rootModel.getModuleLibraryTable()
                    .getModifiableModel();
            Library library = projectLibraryModel.createLibrary("switch:"+switchName,
                    OCamlLibraryKind.INSTANCE);
            Library.ModifiableModel libraryModel = library.getModifiableModel();

            // set roots
            OCamlSourcesOrderRootType SOURCES = OCamlSourcesOrderRootType.getInstance();
            VirtualFile[] files = myJdk.getRootProvider().getFiles(SOURCES);
            for (VirtualFile f: files) {
                libraryModel.addRoot(f, SOURCES);
                libraryModel.addRoot(f, OrderRootType.CLASSES);
            }

            // save
            libraryModel.commit();
            projectLibraryModel.commit();

            Project project = rootModel.getProject();
            ORSettings service = project.getService(ORSettings.class);

            // set up settings default values
            if (service.getOpamLocation().isEmpty()) {
                service.setOpamLocation(homePath);
                service.setIsWsl(homePath.startsWith(WSLDistribution.UNC_PREFIX));
                service.setSwitchName(switchName);
            }
        }
    }

    @NotNull
    public List<String> getSourcePaths() {
        @NonNls final String path = getContentEntryPath() + File.separator + "src";

        // create "src", put it should not be a source folder
        // because that's something for Java classes
        boolean ignored = new File(path).mkdirs();

        return Collections.emptyList();
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