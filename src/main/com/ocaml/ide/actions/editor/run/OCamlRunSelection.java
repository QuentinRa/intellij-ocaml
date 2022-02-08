package com.ocaml.ide.actions.editor.run;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.util.Pass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.refactoring.IntroduceTargetChooser;
import com.intellij.refactoring.introduce.PsiIntroduceTarget;
import com.ocaml.OCamlBundle;
import com.ocaml.icons.OCamlIcons;
import com.ocaml.ide.actions.editor.OCamlEditorActionBase;
import com.ocaml.ide.console.OCamlConsoleRunner;
import com.ocaml.utils.adaptor.SinceIdeVersion;
import com.ocaml.utils.editor.ExtendedEditorActionUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Allow the user to run
 * the selected code
 */
public class OCamlRunSelection extends OCamlEditorActionBase {

    public static final String ACTION_ID = "editor.repl.run.selection.action";

    @SinceIdeVersion(release = "211")
    public OCamlRunSelection() {
        // fix 211, can't use the package icon if not in main/icons
        getTemplatePresentation().setIcon(OCamlIcons.External.ExecuteSelection);
    }

    @Override protected void doActionPerformed(@NotNull AnActionEvent e, OCamlConsoleRunner runner) {
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        if (editor == null) return;
        PsiFile file = e.getData(CommonDataKeys.PSI_FILE);
        if (file == null) return;
        ArrayList<PsiElement> selectedElements = ExtendedEditorActionUtil.getSelectedElements(editor);
        if (selectedElements == null || selectedElements.size() == 0) return;
        // only one, do not ask
        if (selectedElements.size() == 1) {
            runner.processCommand(selectedElements.get(0).getText());
            return;
        }
        // ask which statement?
        List<PsiIntroduceTarget<PsiElement>> targets = selectedElements.stream().map(PsiIntroduceTarget::new).collect(Collectors.toList());
        IntroduceTargetChooser.showIntroduceTargetChooser(editor, targets, new Pass<>() {
            @Override public void pass(PsiIntroduceTarget<PsiElement> choice) {
                // just in case
                if (choice == null || choice.getPlace() == null) return;
                runner.processCommand(choice.getPlace().getText());
            }
       }, OCamlBundle.message("repl.select.a.statement"), 0);
    }
}
