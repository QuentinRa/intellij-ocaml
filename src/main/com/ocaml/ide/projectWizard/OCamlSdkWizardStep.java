package com.ocaml.ide.projectWizard;

import com.intellij.ide.util.projectWizard.*;
import com.intellij.openapi.fileChooser.*;
import com.intellij.openapi.project.*;
import com.intellij.openapi.projectRoots.*;
import com.intellij.openapi.roots.ui.configuration.*;
import com.intellij.openapi.roots.ui.configuration.projectRoot.*;
import com.intellij.openapi.ui.*;
import com.intellij.openapi.util.*;
import com.intellij.openapi.vfs.*;
import com.intellij.ui.*;
import com.ocaml.*;
import com.ocaml.compiler.*;
import com.ocaml.compiler.opam.*;
import com.ocaml.ide.sdk.*;
import com.ocaml.utils.listener.*;
import org.jetbrains.annotations.*;

import javax.swing.*;
import java.awt.*;

/**
 * This class is providing a step to create a project or a module.
 * The only thing we need to do by ourselves is to create an SDK.
 *
 * Either the user will use an already created, or use "add SDK"
 * and select an SDK installed using opam. Actually, as long as the structure
 * of the folder is the same as the one used by opam, it will work.
 *
 * There is an option to create an "opam-like" SDK. The only thing we
 * need for now, is to know where are the sources, and where is the ocaml
 * binary.<br>
 * We will create a folder in ~/.jdk called "ocaml-{version}" with two folders
 * <ul>
 *     <li>bin
 *         <ul>
 *             <li>ocaml -> shortcut to the specified ocaml</li>
 *             <li>ocamlc -> shortcut to ocaml, in the folder of the ocaml</li>
 *         </ul>
 *     </li>
 *     <li>lib
 *         <ul><li>ocaml -> shortcut to the specified folder</li></ul>
 *     </li>
 * </ul>
 *
 * Features of the class
 * <ul>
 *     <li><b>KO</b>: can add/use an SDK using the JdkComboBox</li>
 *     <li><b>KO</b>: can create an SDK using the other fields</li>
 *     <li><b>OK</b>: warning if the "create simple" is selected but the ocaml binary location was installed using opam.</li>
 *     <li><b>OK</b>: prefill the ocaml binary location</li>
 *     <li><b>OK</b>: prefill the ocamlc binary location</li>
 *     <li><b>OK</b>: prefill the ocaml version</li>
 *     <li><b>OK</b>: prefill the sources location</li>
 *     <li><b>OK</b>: fill the ocamlc binary location when ocaml binary location is defined</li>
 *     <li><b>OK</b>: fill the ocaml version when the ocaml binary location is defined</li>
 *     <li><b>KO</b>: add a loading icon</li>
 *     <li><b>KO</b>: add a check (valid/invalid) icon, as we have in CLion</li>
 *     <li><b>KO</b>: check that everything is valid</li>
 *     <li><b>KO</b>: add a warning if the user is trying to open the project without setting an SDK.</li>
 * </ul>
 */
public class OCamlSdkWizardStep extends ModuleWizardStep {
    @NotNull private final WizardContext myWizardContext;
    @NotNull private final OCamlModuleBuilder myModuleBuilder;
    @NotNull private JPanel myPanel;
    private Project myProject;

    @SuppressWarnings("unused") private PanelWithText myInstructionsLabel;
    private JLabel myLabelSdk; // to prompt "Project" or "Module"

    // choice
    private ButtonGroup myUseSdkChoice;
    private boolean isUseSelected;
    // use
    @NotNull private JdkComboBox myJdkChooser;
    // create
    private JPanel myUseComponents;
    private JPanel myCreateComponents;
    private JLabel myOcamlVersion;
    private TextFieldWithBrowseButton mySdkSources;
    private TextFieldWithBrowseButton myOCamlLocation;
    private JLabel myOCamlCompilerLocation;
    private JLabel myOpamWarning;

    public OCamlSdkWizardStep(@NotNull WizardContext wizardContext,
                              @NotNull OCamlModuleBuilder moduleBuilder) {
        myWizardContext = wizardContext;
        myModuleBuilder = moduleBuilder;

        // are we inside a project?
        boolean isProject = myWizardContext.getProject() == null;
        myLabelSdk.setText(OCamlBundle.message("module.prompt.sdk", isProject ? "Project" : "Module"));
        myLabelSdk.setLabelFor(myJdkChooser);

        // Disable create
        setEnabledPanel(myCreateComponents, false);
        isUseSelected = true;

        // Add the listener on one choice, triggered
        // when the button is enabled/disabled
        // Enable/Disable the right panel
        myUseSdkChoice.getSelection().addItemListener((c) -> {
            isUseSelected = !isUseSelected;
            setEnabledPanel(myCreateComponents, !isUseSelected);
            setEnabledPanel(myUseComponents, isUseSelected);
            showWarning();
        });

        // Detect and prefill fields
        OCamlDetector.detectBinaries(output -> {
            myOCamlLocation.setText(output.ocaml);
            myOCamlCompilerLocation.setText(output.ocamlCompiler);
            mySdkSources.setText(output.sources);
            myOcamlVersion.setText(output.version);
        });

        // On Field Updated (manually)
        DeferredDocumentListener.addDeferredDocumentListener(
                myOCamlLocation.getTextField(),
                e -> onOCamlLocationChange(), 1000);
        // On File selected
        myOCamlLocation.addBrowseFolderListener(
                new TextBrowseFolderListener(FileChooserDescriptorFactory.createSingleFolderDescriptor(), myProject) {
                    @SuppressWarnings("UnstableApiUsage")
                    @Override protected void onFileChosen(@NotNull VirtualFile chosenFile) {
                        super.onFileChosen(chosenFile);
                        onOCamlLocationChange();
                    }
                }
        );

        // Set the foreground using a JBColor
        myOpamWarning.setForeground(JBColor.RED);
    }

    // update the two other labels once the ocaml location was set
    private void onOCamlLocationChange() {
        String path = myOCamlLocation.getText();
        myOCamlCompilerLocation.setText(path.replace("/bin/ocaml", "/bin/ocamlc"));
        OCamlUtils.ocamlCompilerVersion(myOCamlCompilerLocation.getText(), v -> {
            myOcamlVersion.setText(v);
            showWarning();
        });
    }

    // show the warning "opam ..." if needed
    private void showWarning() {
        boolean show = !isUseSelected && OpamUtils.isOpamBinary(myOCamlCompilerLocation.getText());
        myOpamWarning.setVisible(show);
    }

    // re-enable every component in a panel
    private void setEnabledPanel(JPanel panel, boolean enabled) {
        for (Component component : panel.getComponents()) {
            component.setEnabled(enabled);
        }
    }

    @Override public JComponent getComponent() {
        return myPanel;
    }

    @Override public void updateDataModel() {
        Sdk sdk;
        if (!isUseSelected) {
            throw new UnsupportedOperationException("creating SDK not supported yet.");
        } else {
            sdk = myJdkChooser.getSelectedJdk();
        }

        // are we inside a project?
        boolean isProject = myWizardContext.getProject() == null;

        if (isProject) {
            myWizardContext.setProjectJdk(sdk);
        } else {
            myModuleBuilder.setModuleJdk(sdk);
        }
    }

    // create components, needed by the ".form" since some must be created manually.
    // called before the rest of the constructor, after initializing the first two variables
    // that's why this code can't be moved in the constructor.
    // Moreover, variables that are initialized by the ".form" are also null here.
    public void createUIComponents() {
        Project project = myWizardContext.getProject();
        project = project != null ? project : ProjectManager.getInstance().getDefaultProject();
        myProject = project;

        final ProjectStructureConfigurable projectConfig = ProjectStructureConfigurable.getInstance(project);
        ProjectSdksModel sdksModel = projectConfig.getProjectJdksModel();

        SdkType type = OCamlSdkType.getInstance();
        Condition<? super SdkTypeId> sdkTypeFilter = sdk -> sdk instanceof SdkType && (type == null || type.equals(sdk));

        myJdkChooser = new JdkComboBox(project,
                sdksModel,
                sdkTypeFilter,
                null,
                null,
                null);

        // adding the instructions
        myInstructionsLabel = new PanelWithText(OCamlBundle.message("project.wizard.instruction"));
    }
}
