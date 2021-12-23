package com.ocaml.ide.settings;

import com.intellij.execution.wsl.*;
import com.intellij.openapi.fileChooser.*;
import com.intellij.openapi.options.*;
import com.intellij.openapi.project.*;
import com.intellij.openapi.projectRoots.*;
import com.intellij.openapi.roots.*;
import com.intellij.openapi.ui.*;
import com.intellij.openapi.vfs.*;
import com.intellij.util.ui.*;
import com.ocaml.ide.sdk.*;
import org.jetbrains.annotations.*;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class ORSettingsConfigurable implements SearchableConfigurable, Configurable.NoScroll {
    private final @NotNull Project myProject;
    private ORSettings mySettings;
    private final List<String[]> myEnv = new ArrayList<>();

    private JPanel myRootPanel;
    private JTabbedPane myTabs;

    // Opam
    private TextFieldWithBrowseButton myOpamLocation;
    private boolean myIsWsl = false;
    private JComboBox<String> mySwitchSelect;
    private JLabel myDetectionLabel;
    private JTable myOpamLibraries;

    public ORSettingsConfigurable(@NotNull Project project) {
        myProject = project;
    }

    @NotNull
    @Override
    public String getId() {
        return getHelpTopic();
    }

    @NotNull
    @Nls
    @Override
    public String getDisplayName() {
        return "OCaml";
    }

    @NotNull
    @Override
    public String getHelpTopic() {
        return "settings.ocaml";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        mySettings = myProject.getService(ORSettings.class);
        createOpamTab();


        mySwitchSelect.addItemListener(itemEvent -> {
            if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
                String version = (String) itemEvent.getItem();
                clearEnv();
                listLibraries(version);
            }
        });

        myOpamLibraries.setBorder(BorderFactory.createLineBorder(JBUI.CurrentTheme.DefaultTabs.borderColor()));
        listLibraries(mySettings.getSwitchName());

        return myRootPanel;
    }

    @Override
    public void apply() {
        // Opam
        mySettings.setOpamLocation(sanitizeInput(myOpamLocation));
        mySettings.setIsWsl(myIsWsl);
        mySettings.setSwitchName((String) mySwitchSelect.getSelectedItem());

        //// Create external library based on the selected opam switch
        //createExternalLibraryDependency(mySettings);
        //// Compute env
        //OpamEnv opamEnv = myProject.getService(OpamEnv.class);
        //opamEnv.computeEnv(mySettings.getOpamLocation(), mySettings.getSwitchName(), mySettings.getCygwinBash(), null);
        //// Display compiler info in console (if any)
        //myProject.getService(ORToolWindowManager.class).showShowToolWindows();
    }

    private void createExternalLibraryDependency(ORSettings settings) {
        //Project project = settings.getProject();
        //
        //LibraryTable projectLibraryTable = LibraryTablesRegistrar.getInstance().getLibraryTable(project);
        //LibraryTable.ModifiableModel projectLibraryTableModel = projectLibraryTable.getModifiableModel();
        //
        //String libraryName = "switch:" + settings.getSwitchName();
        //
        //// Remove existing lib
        //Library oldLibrary = projectLibraryTableModel.getLibraryByName(libraryName);
        //VirtualFile opamRootCandidate = VirtualFileManager.getInstance().findFileByNioPath(Path.of(settings.getOpamLocation(), settings.getSwitchName()));
        //if (opamRootCandidate != null && opamRootCandidate.exists() && opamRootCandidate.isValid()) {
        //    Library library = oldLibrary == null ? projectLibraryTableModel.createLibrary(libraryName, OclLibraryKind.INSTANCE) : null;
        //    Library.ModifiableModel libraryModel = library == null ? null : library.getModifiableModel();
        //
        //    if (libraryModel != null) {
        //        OclLibraryType libraryType = (OclLibraryType) LibraryType.findByKind(OclLibraryKind.INSTANCE);
        //        LibraryRootsComponentDescriptor rootsComponentDescriptor = libraryType.createLibraryRootsComponentDescriptor();
        //        List<OrderRoot> orderRoots = RootDetectionUtil.detectRoots(Collections.singleton(opamRootCandidate), myRootPanel, project, rootsComponentDescriptor);
        //        for (OrderRoot orderRoot : orderRoots) {
        //            libraryModel.addRoot(orderRoot.getFile(), orderRoot.getType());
        //        }
        //    }
        //
        //    ApplicationManager.getApplication().invokeAndWait(() -> WriteAction.run(() -> {
        //        if (libraryModel != null) {
        //            libraryModel.commit();
        //            projectLibraryTableModel.commit();
        //        }
        //
        //        // Find module that contains dune config root file
        //        Map<Module, VirtualFile> duneContentRoots = Platform.findContentRootsFor(project, DunePlatform.DUNE_PROJECT_FILENAME);
        //        for (Module module : duneContentRoots.keySet()) {
        //            ModuleRootModificationUtil.updateModel(module, moduleModel -> {
        //                // Remove all libraries entries that are of type Ocaml
        //                moduleModel.orderEntries().forEach(entry -> {
        //                    Library entryLibrary = (entry instanceof LibraryOrderEntry) ? ((LibraryOrderEntry) entry).getLibrary() : null;
        //                    PersistentLibraryKind<?> entryLibraryKind = (entryLibrary instanceof LibraryBridge) ? ((LibraryBridge) entryLibrary).getKind() : null;
        //                    if (entryLibraryKind instanceof OclLibraryKind) {
        //                        moduleModel.removeOrderEntry(entry);
        //                    }
        //
        //                    return true;
        //                });
        //                // Add the new lib as order entry
        //                moduleModel.addLibraryEntry(library == null ? oldLibrary : library);
        //            });
        //        }
        //    }));
        //}
    }

    private void listLibraries(@NotNull String version) {
        //myProject.getService(OpamProcess.class)
        //        .list(myOpamLocation.getText(), version, myCygwinBash, libs -> {
        //            myEnv.clear();
        //            if (libs != null) {
        //                myEnv.addAll(libs);
        //            }
        //            myOpamLibraries.setModel(createDataModel());
        //        });
    }

    @NotNull
    private AbstractTableModel createDataModel() {
        return new AbstractTableModel() {
            @Override
            public int getRowCount() {
                return myEnv.size();
            }

            @Override
            public int getColumnCount() {
                return 3;
            }

            @Override
            public @NotNull Object getValueAt(int rowIndex, int columnIndex) {
                String[] columns = myEnv.get(rowIndex);
                return columns.length <= columnIndex ? "" : columns[columnIndex];
            }
        };
    }

    private void clearEnv() {
        myEnv.clear();
        myOpamLibraries.setModel(createDataModel());
    }

    @Override
    public boolean isModified() {
        // Opam
        boolean isOpamLocationModified =
                !myOpamLocation.getText().equals(mySettings.getOpamLocation());
        boolean isOpamSwitchModified = !mySettings.getSwitchName().equals(mySwitchSelect.getSelectedItem());
        return isOpamLocationModified || isOpamSwitchModified;
    }

    @Override
    public void reset() {
        // Opam
        String opamLocation = mySettings.getOpamLocation();
        if (opamLocation.isEmpty()) { // check if there is an OCaml SDK
            Sdk projectSdk = ProjectRootManager.getInstance(mySettings.getProject()).getProjectSdk();
            if (projectSdk != null) {
                VirtualFile home = projectSdk.getHomeDirectory();
                if (home != null) {
                    opamLocation = home.getParent().getPath();
                }
            }
            myIsWsl = opamLocation.replace("/", "\\").startsWith(WSLDistribution.UNC_PREFIX);
            // update "default"
            mySettings.setIsWsl(myIsWsl);
            mySettings.setOpamLocation(opamLocation);
        }
        myOpamLocation.setText(opamLocation);
        myIsWsl = mySettings.isWsl();

        setDetectionText();
        createSwitch(opamLocation, mySettings.getSwitchName());
    }

    private void setDetectionText() {
        if (myIsWsl) {
            myDetectionLabel.setText("WSL detected");
        } else {
            myDetectionLabel.setText("");
        }
    }

    private void createOpamTab() {
        Project project = mySettings.getProject();
        myOpamLocation.addBrowseFolderListener(
                new TextBrowseFolderListener(FileChooserDescriptorFactory.createSingleFolderDescriptor(), project) {
                    @Override
                    protected void onFileChosen(@NotNull VirtualFile chosenFile) {
                        super.onFileChosen(chosenFile);

                        // Try to detect specific os implementation
                        String path = chosenFile.getPath();
                        myIsWsl = path.replace("/", "\\").startsWith(WSLDistribution.UNC_PREFIX);
                        setDetectionText();

                        // Build a list of switch
                         createSwitch(path, "");
                    }
                });
    }

    private void createSwitch(@NotNull String opamLocation, @NotNull String current) {
        File[] opamSwitches = new File(opamLocation).listFiles();
        mySwitchSelect.removeAllItems();
        // todo: check after filtering...
        mySwitchSelect.setEnabled(opamSwitches != null && opamSwitches.length != 0);
        if (opamSwitches != null) {
            for (File f : opamSwitches) {
                if (!f.canRead() || f.isFile()) continue;
                // todo: should not do this, and check OpamUtils, same problem
                if (OCamlSdkType.VERSION_REGEXP.matcher(f.getPath().replace("\\", "/")).matches()) {
                    mySwitchSelect.addItem(f.getName());
                }
            }
        }
    }

    private static @NotNull String sanitizeInput(@NotNull TextFieldWithBrowseButton textFieldWithBrowseButton) {
        return sanitizeInput(textFieldWithBrowseButton.getText());
    }

    private static @NotNull String sanitizeInput(@NotNull String input) {
        return input.trim();
    }
}
