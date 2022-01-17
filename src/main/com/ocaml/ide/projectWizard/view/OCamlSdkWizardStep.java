package com.ocaml.ide.projectWizard.view;

import com.intellij.*;
import com.intellij.ide.*;
import com.intellij.ide.util.projectWizard.*;
import com.intellij.openapi.fileChooser.*;
import com.intellij.openapi.options.*;
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
import com.ocaml.ide.projectWizard.*;
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
 *     <li><b>OK</b>: can add/use an SDK using the JdkComboBox</li>
 *     <li><b>OK</b>: the JDKComboBox must be loaded with the existing JDKs</li>
 *     <li><b>OK</b>: can create an SDK using the other fields</li>
 *     <li><b>OK</b>: warning if the "create simple" is selected but the ocaml binary location was installed using opam.</li>
 *     <li><b>OK</b>: prefill the ocaml binary location ({@link OCamlDetector#detectBinaries()})</li>
 *     <li><b>OK</b>: prefill the ocamlc binary location ({@link OCamlDetector#detectBinaries()})</li>
 *     <li><b>OK</b>: prefill the ocaml version ({@link OCamlDetector#detectBinaries()})</li>
 *     <li><b>OK</b>: prefill the sources location ({@link OCamlDetector#detectBinaries()})</li>
 *     <li><b>OK</b>: fill the ocamlc binary location when ocaml binary location is defined ({@link OCamlDetector#findAssociatedBinaries})</li>
 *     <li><b>OK</b>: fill the ocaml version when the ocaml binary location is defined ({@link OCamlDetector#findAssociatedBinaries})</li>
 *     <li><b>KO</b>: add a loading icon and a check (valid/invalid) icon, as we have in CLion</li>
 *     <li><b>OK</b>: check that everything is valid</li>
 *     <li><b>OK</b>: add a warning if the user is trying to open the project without setting an SDK.</li>
 *     <li><b>OK</b>: handle possible bug if the user is pressing next while the async codes was not finished</li>
 *     <li><b>OK</b>: add a label "an opam-like SDK will be created in ~/.jdks"</li>
 *     <li><b>OK</b>: if we detect an "cygwin" SDK, then update libs with /lib/ocaml ({@link OCamlDetector#findAssociatedBinaries})</li>
 *     <li><b>OK</b>: allow the user of ocaml.exe</li>
 *     <li><b>OK</b>: messages with a path (ex: expected "/bin/ocaml"), should be changed if the path was \\bin\\ocaml)</li>
 * </ul>
 */
public class OCamlSdkWizardStep extends ModuleWizardStep {
    @NotNull private final WizardContext myWizardContext;
    @NotNull private final OCamlModuleBuilder myModuleBuilder;
    @NotNull private JPanel myPanel;
    private Project myProject;

    @SuppressWarnings("unused") private PanelWithText myInstructionsLabel;
    private JLabel myLabelSdk; // to prompt "Project" or "Module"

    @NotNull private ButtonGroup myUseSdkChoice; // the group of two buttons
    private boolean isUseSelected; // true if the first menu is selected, false else
    @NotNull private JdkComboBox myJdkChooser; // 1# select JDK
    @NotNull private JPanel myUseComponents; // 2# to disable every component in the second menu
    @NotNull private JPanel myCreateComponents; // 1# to disable every component in the second menu
    @NotNull private JLabel myOcamlVersion; // 2# show ocaml version, fetched from myOCamlLocation
    @NotNull private TextFieldWithBrowseButton mySdkSources; // 2# submit sources
    @NotNull private TextFieldWithBrowseButton myOCamlLocation; // 2# submit ocaml binary location
    @NotNull private JLabel myOCamlCompilerLocation; // 2# show compiler location deduced using myOCamlLocation
    @NotNull private JLabel myOpamWarning; // 2# show a warning if using opam in 2#, should be in 1#
    @NotNull private JLabel myWizardTitle; // title of the wizard
    @NotNull private JLabel myCreateLocationLabel; // 2# show were the created sdk will be stored
    @Nullable private Sdk createSDK; // 2# the sdk that we created
    private ProjectSdksModel mySdksModel;
    private OCamlSdkUtils.CustomCamlSdkData myCustomSdkData; // 2# data of the SDK we are about to create
    boolean shouldValidateAgain = true;

    public OCamlSdkWizardStep(@NotNull WizardContext wizardContext,
                              @NotNull OCamlModuleBuilder moduleBuilder) {
        myWizardContext = wizardContext;
        myModuleBuilder = moduleBuilder;

        // are we inside a project?
        boolean isProject = myWizardContext.getProject() == null;
        String word = OCamlBundle.message(isProject ? "project.up.first" : "module.up.first");
        myLabelSdk.setText(OCamlBundle.message("module.prompt.sdk", word));
        myWizardTitle.setText(OCamlBundle.message("project.wizard.title", word));
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
        OCamlDetector.DetectionResult output = OCamlDetector.detectBinaries();
        if (output != null) {
            myOCamlLocation.setText(output.ocaml);
            myOCamlCompilerLocation.setText(output.ocamlCompiler);
            mySdkSources.setText(output.sources);
            myOcamlVersion.setText(output.version);
        }

        // On Field Updated (manually)
        DeferredDocumentListener.addDeferredDocumentListener(
                myOCamlLocation.getTextField(),
                e -> onOCamlLocationChange(), 1000);
        // On File selected
        myOCamlLocation.addBrowseFolderListener(
                new TextBrowseFolderListener(FileChooserDescriptorFactory.createSingleFileDescriptor(), myProject) {
                    @SuppressWarnings("UnstableApiUsage")
                    @Override protected void onFileChosen(@NotNull VirtualFile chosenFile) {
                        super.onFileChosen(chosenFile);
                        onOCamlLocationChange();
                    }
                }
        );

        // Set the foreground using a JBColor
        myOpamWarning.setForeground(JBColor.RED);

        // set the text + color
        myCreateLocationLabel.setText(OCamlBundle.message("project.wizard.create.location", OCamlSdkUtils.JDK_FOLDER));
        myCreateLocationLabel.setForeground(JBColor.GRAY);
    }

    // update the two other labels once the ocaml location was set
    private void onOCamlLocationChange() {
        // must validate again as the path changed
        shouldValidateAgain = true;
        // get the ocaml binary path
        String path = myOCamlLocation.getText();
        // we are waiting for a version
        myOcamlVersion.setText("");
        // Ask for the values
        OCamlDetector.findAssociatedBinaries(path, arg -> {
            myOCamlCompilerLocation.setText(arg.ocamlCompiler);
            myOcamlVersion.setText(arg.version);
            mySdkSources.setText(arg.sources);
            // opam warning
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
        Sdk sdk = getSdk();

        // are we inside a project?
        boolean isProject = myWizardContext.getProject() == null;

        if (isProject) {
            myWizardContext.setProjectJdk(sdk);
        } else {
            myModuleBuilder.setModuleJdk(sdk);
        }
    }

    private Sdk getSdk() { return isUseSelected ? myJdkChooser.getSelectedJdk() : createSDK; }

    @Override public @Nullable Icon getIcon() {
        return myWizardContext.getStepIcon();
    }

    /**
     * @see ProjectJdkForModuleStep
     */
    @Override public boolean validate() throws ConfigurationException {
        boolean sdkSelected = true;
        if (isUseSelected) {
            if (myJdkChooser.isProjectJdkSelected()) return true;
            sdkSelected = myJdkChooser.getSelectedJdk() != null;
        }
        else {
            if (!shouldValidateAgain) return true;
            myCustomSdkData = OCamlSdkUtils.createSdk(
                    myOCamlLocation.getText(),
                    myOcamlVersion.getText(),
                    myOCamlCompilerLocation.getText(),
                    mySdkSources.getText()
            );
            shouldValidateAgain = false;
        }

        if (!sdkSelected) {
            int result = Messages.showOkCancelDialog(JavaUiBundle.message("prompt.confirm.project.no.jdk"),
                            JavaUiBundle.message("title.no.jdk.specified"),
                            Messages.getOkButton(),
                            Messages.getCancelButton(),
                            Messages.getWarningIcon());
            return result == Messages.OK;
        }
        if (!isUseSelected) {
            mySdksModel.addSdk(
                    myCustomSdkData.type,
                    myCustomSdkData.homePath,
                    sdk -> createSDK = sdk // save
            );
            // reload when a new Sdk is added
            myJdkChooser.reloadModel();
        }

        try {
            mySdksModel.apply(null, true);
        } catch (ConfigurationException e) {
            // We should allow Next step if user has wrong SDK
            if (Messages.showDialog(JavaUiBundle.message("dialog.message.0.do.you.want.to.proceed", e.getMessage()),
                    e.getTitle(),
                    new String[]{CommonBundle.getYesButtonText(), CommonBundle.getNoButtonText()},
                    1, Messages.getWarningIcon()) != Messages.YES) {
                return false;
            }
        }

        return true;
    }

    // create components, needed by the ".form" since some must be created manually.
    // called before the rest of the constructor, after initializing the first two variables
    // that's why this code can't be moved in the constructor.
    // Moreover, variables that are initialized by the ".form" are also null here.
    public void createUIComponents() {
        myProject = myWizardContext.getProject();
        myProject = myProject != null ? myProject : ProjectManager.getInstance().getDefaultProject();

        final ProjectStructureConfigurable projectConfig = ProjectStructureConfigurable.getInstance(myProject);
        mySdksModel = projectConfig.getProjectJdksModel();
        mySdksModel.reset(myProject);

        SdkType type = OCamlSdkType.getInstance();
        Condition<? super SdkTypeId> sdkTypeFilter = sdk -> sdk instanceof SdkType && (type == null || type.equals(sdk));

        myJdkChooser = new JdkComboBox(myProject,
                mySdksModel,
                sdkTypeFilter,
                null,
                null,
                null);
        // show if we are inside a module
        if (myProject != null) myJdkChooser.showProjectSdkItem();

        // adding the instructions
        myInstructionsLabel = new PanelWithText(OCamlBundle.message("project.wizard.instruction"));
    }
}