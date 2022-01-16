package com.ocaml.ide.projectWizard;

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
 *     <li><b>O</b>K: the JDKComboBox must be loaded with the existing JDKs</li>
 *     <li><b>OK</b>: can create an SDK using the other fields</li>
 *     <li><b>OK</b>: warning if the "create simple" is selected but the ocaml binary location was installed using opam.</li>
 *     <li><b>OK</b>: prefill the ocaml binary location</li>
 *     <li><b>OK</b>: prefill the ocamlc binary location</li>
 *     <li><b>OK</b>: prefill the ocaml version</li>
 *     <li><b>OK</b>: prefill the sources location</li>
 *     <li><b>OK</b>: fill the ocamlc binary location when ocaml binary location is defined</li>
 *     <li><b>OK</b>: fill the ocaml version when the ocaml binary location is defined</li>
 *     <li><b>KO</b>: add a loading icon</li> and a check (valid/invalid) icon, as we have in CLion</li>
 *     <li><b>OK</b>: check that everything is valid</li>
 *     <li><b>OK</b>: add a warning if the user is trying to open the project without setting an SDK.</li>
 *     <li><b>OK</b>: handle possible bug if the user is pressing next while the async codes was not finished</li>
 *     <li><b>KO</b>: add a label "an opam-like SDK will be created in ~/.jdks"
 *     <li><b>KO</b>: if we detect an "cygwin" SDK, then update libs with /lib/ocaml
 *     <li><b>KO</b>: allow the user of ocaml.exe
 *     <li><b>KO</b>: messages with a path (ex: expected "/bin/ocaml"), should be changed if the path was \\bin\\ocaml)
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
    private JLabel myWizardTitle;
    private Sdk createSDK;
    private ProjectSdksModel mySdksModel;
    private OCamlSdkUtils.CustomCamlSdkData myCustomSdkData;

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
    }

    // update the two other labels once the ocaml location was set
    private void onOCamlLocationChange() {
        String path = myOCamlLocation.getText();
        // Linux
        myOCamlCompilerLocation.setText(path.replace("/bin/ocaml", "/bin/ocamlc"));
        // Windows
        myOCamlCompilerLocation.setText(path.replace("\\bin\\ocaml", "\\bin\\ocamlc"));
        // we are waiting for a version
        myOcamlVersion.setText("");
        OCamlUtils.ocamlCompilerVersion(myOCamlCompilerLocation.getText(), v -> myOcamlVersion.setText(v));
        // opam warning
        showWarning();
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

    @Override public void updateStep() {
    }

    @Override public @Nullable Icon getIcon() {
        return myWizardContext.getStepIcon();
    }

    /**
     * @see ProjectJdkForModuleStep
     */
    @Override public boolean validate() throws ConfigurationException {
        //final Sdk sdk;
        boolean sdkSelected = true;
        if (isUseSelected) sdkSelected = myJdkChooser.getSelectedJdk() != null;
        else {
            myCustomSdkData = OCamlSdkUtils.createSdk(
                    myOCamlLocation.getText(),
                    myOcamlVersion.getText(),
                    myOCamlCompilerLocation.getText(),
                    mySdkSources.getText()
            );
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

        // adding the instructions
        myInstructionsLabel = new PanelWithText(OCamlBundle.message("project.wizard.instruction"));
    }
}
