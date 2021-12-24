package com.ocaml.ide.settings;

import com.intellij.execution.wsl.*;
import com.intellij.openapi.fileChooser.*;
import com.intellij.openapi.options.*;
import com.intellij.openapi.project.*;
import com.intellij.openapi.ui.*;
import com.intellij.openapi.vfs.*;
import com.intellij.util.ui.*;
import com.ocaml.comp.opam.*;
import com.ocaml.comp.opam.process.*;
import org.jetbrains.annotations.*;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.*;
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
    }

    private void listLibraries(@NotNull String version) {
        myProject.getService(OpamService.class)
                .list(myOpamLocation.getText(), version, libs -> {
                    myEnv.clear();
                    if (libs != null) {
                        myEnv.addAll(libs);
                    }
                    myOpamLibraries.setModel(createDataModel());
                });
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
        if (!myOpamLocation.getText().isEmpty()) {
            myProject.getService(OpamService.class).listSwitch(opamLocation, l -> {
                boolean found = false;

                // reset
                mySwitchSelect.removeAllItems();
                mySwitchSelect.setEnabled(l != null && !l.isEmpty());

                if (l != null) {
                    for (FindSwitchProcess.OpamSwitch s: l) {
                        mySwitchSelect.addItem(s.name);
                        if (s.name.equals(current)) found = true;
                    }
                    if (found) {
                        mySwitchSelect.setSelectedItem(current);
                    }
                }
            });
        }
    }

    private static @NotNull String sanitizeInput(@NotNull TextFieldWithBrowseButton textFieldWithBrowseButton) {
        return sanitizeInput(textFieldWithBrowseButton.getText());
    }

    private static @NotNull String sanitizeInput(@NotNull String input) {
        return input.trim();
    }
}
