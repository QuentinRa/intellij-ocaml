package com.ocaml.ide.sdk;

import com.intellij.notification.*;
import com.intellij.openapi.projectRoots.*;
import com.intellij.openapi.roots.*;
import com.intellij.openapi.roots.ui.configuration.projectRoot.*;
import com.intellij.openapi.util.*;
import com.intellij.openapi.util.io.*;
import com.intellij.openapi.vfs.*;
import com.ocaml.comp.opam.*;
import com.ocaml.comp.opam.process.*;
import com.ocaml.ide.sdk.sources.*;
import com.ocaml.utils.*;
import icons.*;
import org.jdom.*;
import org.jetbrains.annotations.*;

import javax.swing.*;
import java.io.*;
import java.util.*;
import java.util.function.*;
import java.util.regex.*;

/**
 * Everything related to the SDK.
 * => name, icon, version
 * => suggestHomePaths: detect existing SDK
 * => isValidSdkHome: check that the selected SDK home is valid
 * => getDefaultDocumentationUrl (unused): may be used to fetch the sources
 * => setupSdkPaths: look for the source roots of the SDK and "import/index" them.
 * If no sources are found, a message is shown.
 */
public class OCamlSdkType extends SdkType implements SdkDownload {

    public static final String ID = "OCaml SDK";
    public static final Pattern VERSION_REGEXP = Pattern.compile(".*/(\\d\\.\\d\\d(\\.\\d)?)/?.*");
    public static final String ADD_SOURCES_POPUP = "<html>"
            + "To enjoy most of the plugin features, you need to set up the sources.\n"
            + "see <a href=\"https://giraud.github.io/reasonml-idea-plugin/docs/language-support/ocaml\">the documentation</a>."
            + "</html>";
    private static final String UNKNOWN_VERSION = "unknown version";

    public OCamlSdkType() {
        super(ID);
    }

    @NotNull
    public static SdkType getInstance() {
        return SdkType.findInstance(OCamlSdkType.class);
    }

    @NotNull @Override public String getPresentableName() {
        return "OCaml";
    }

    @NotNull @Override public Icon getIcon() {
        return ORIcons.OCL_SDK;
    }

    @Override public @NotNull Collection<String> suggestHomePaths() {
        Collection<FindSwitchProcess.OpamSwitch> foundPaths = OpamUtils.findExistingSDKs();
        ArrayList<String> paths = new ArrayList<>();
        foundPaths.forEach(s -> paths.add(s.path));
        paths.sort((o1, o2) -> Comparing.compare(o2, o1));
        return paths;
    }

    @Nullable @Override public String suggestHomePath() {
        return null;
    }

    @Override public boolean isValidSdkHome(@NotNull String sdkHome) {
        return !getVersionString(sdkHome).equals(UNKNOWN_VERSION);
    }

    @NotNull @Override public String getVersionString(@NotNull String sdkHome) {
        // read the version in the name
        String serialized = sdkHome.replace("\\", "/");
        Matcher m1 = VERSION_REGEXP.matcher(serialized);
        if (m1.matches()) {
            return m1.group(1);
        }
        return UNKNOWN_VERSION;
    }

    @NotNull @Override public String suggestSdkName(String currentSdkName, @NotNull String sdkHome) {
        return "OCaml-" + getVersionString(sdkHome);
    }

    @Override public boolean isRootTypeApplicable(@NotNull OrderRootType type) {
        return type.name().equals("OCAML_SOURCES");
    }

    // add features

    @Override public @Nullable AdditionalDataConfigurable createAdditionalDataConfigurable(@NotNull SdkModel sdkModel, @NotNull SdkModificator sdkModificator) {
        return null;
    }

    // Index sources

    @Override public void saveAdditionalData(@NotNull SdkAdditionalData additionalData, @NotNull Element additional) {
    }

    @Override public @Nullable String getDefaultDocumentationUrl(@NotNull Sdk sdk) {
        String version = getVersionString(sdk);
        if (version == null) {
            return null;
        }
        // remove ".{minor}"
        String release = version.substring(0, version.length() - 3);
        // ex: https://caml.inria.fr/pub/distrib/ocaml-4.12/ocaml-4.12.0.tar.gz
        return "https://caml.inria.fr/pub/distrib/ocaml-" + release + "/ocaml-" + version + ".tar.gz";
    }

    @Override public boolean setupSdkPaths(@NotNull Sdk sdk, @NotNull SdkModel sdkModel) {
        return super.setupSdkPaths(sdk, sdkModel);
    }

    @Override public void setupSdkPaths(@NotNull Sdk sdk) {
        String homePath = sdk.getHomePath();
        if (homePath == null) {
            return;
        }
        File jdkHome = new File(homePath);
        SdkModificator sdkModificator = sdk.getSdkModificator();
        sdkModificator.removeRoots(OrderRootType.CLASSES);
        addSources(jdkHome, sdkModificator, sdk.getVersionString());
        sdkModificator.commitChanges();
    }

    // added back to prevent breaking things
    @Deprecated
    public static void reindexSourceRoots(Sdk projectSdk) {
        if (projectSdk.getSdkType() instanceof OCamlSdkType) {
            ((OCamlSdkType)projectSdk.getSdkType()).setupSdkPaths(projectSdk);
        }
    }

    /**
     * Update roots, when manually setting sources
     */
    public void updateSdkPaths(@NotNull Sdk sdk, List<VirtualFile> result) {
        SdkModificator sdkModificator = sdk.getSdkModificator();
        sdkModificator.removeRoots(OrderRootType.CLASSES);
        OrderRootType OCAML_SOURCES = OCamlSourcesOrderRootType.getInstance();
        for (VirtualFile file : result) {
            sdkModificator.addRoot(file, OCAML_SOURCES);
            sdkModificator.addRoot(file, OrderRootType.CLASSES);
        }
        sdkModificator.commitChanges();
    }

    public static void addSources(@NotNull File jdkHome, @NotNull SdkModificator sdkModificator, @Nullable String version) {
        // todo: what if there are multiples roots for sources?
        //  (ie. the user installed extensions)
        String rootPath = getSourceRootPath(jdkHome, version);
        if (rootPath == null) {
            // show notification so that the user know that he/she must add sources manually
            var notification = new ORNotifications.OCamlNotificationData(ADD_SOURCES_POPUP);
            notification.myTitle = "OCamlSDK";
            notification.mySubtitle = "Sources are missing";
            notification.myListener = NotificationListener.URL_OPENING_LISTENER;
            notification.myIcon = ORIcons.OCL_SDK;
            ORNotifications.notify(notification);
            return;
        }
        VirtualFile rootCandidate = LocalFileSystem
                .getInstance()
                .findFileByPath(FileUtil.toSystemIndependentName(rootPath));
        if (rootCandidate == null) {
            return;
        }

        OrderRootType OCAML_SOURCES = OCamlSourcesOrderRootType.getInstance();
        Collection<VirtualFile> files = OCamlRootsDetector.suggestOCamlRoots(rootCandidate);
        for (VirtualFile file : files) {
            sdkModificator.addRoot(file, OCAML_SOURCES);
            sdkModificator.addRoot(file, OrderRootType.CLASSES);
        }
    }

    private static String getSourceRootPath(File sdkHome, String version) {
        return OpamUtils.getOpamSDKSourceFolder(sdkHome, version);
    }

    //
    // Download
    //

     @Override public boolean supportsDownload(@NotNull SdkTypeId sdkTypeId) {
        return false; //sdkTypeId.getName().equals(ID);
    }

     @Override public void showDownloadUI(@NotNull SdkTypeId sdkTypeId, @NotNull SdkModel sdkModel, @NotNull JComponent parentComponent, @Nullable Sdk selectedSdk, @NotNull Consumer<SdkDownloadTask> sdkCreatedCallback) {
        //DataContext dataContext = DataManager.getInstance().getDataContext(parentComponent);
        //Project project = CommonDataKeys.PROJECT.getData(dataContext);
        //if (project != null && project.isDisposed()) return;
        //
        // //ProjectSdksModel#doDownload
        // //#createDownloadActions
        // //mySdkTypeCreationFilter
        // //SdkDownload
        // //com.intellij.openapi.projectRoots.impl.jdkDownloader.JdkDownloader
        //
        //Messages.showErrorDialog(project,
        //        "No SDK available for download yet.",
        //        "Download OCaml SDK"
        //);
    }
}
