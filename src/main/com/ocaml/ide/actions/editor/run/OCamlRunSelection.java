package com.ocaml.ide.actions.editor.run;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.util.Pass;
import com.intellij.psi.PsiElement;
import com.intellij.refactoring.IntroduceTargetChooser;
import com.intellij.refactoring.introduce.PsiIntroduceTarget;
import com.ocaml.OCamlBundle;
import com.ocaml.icons.OCamlIcons;
import com.ocaml.ide.actions.editor.OCamlEditorActionBase;
import com.ocaml.ide.console.OCamlConsoleRunner;
import com.ocaml.lang.core.PsiLetWithAnd;
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

    public OCamlRunSelection() {
        @SinceIdeVersion(release = "211", note = "can't use the package icon if not in main/icons")
        Presentation templatePresentation = getTemplatePresentation();
        templatePresentation.setIcon(OCamlIcons.External.ExecuteSelection);
    }

    @Override protected void doActionPerformed(@NotNull AnActionEvent e, OCamlConsoleRunner runner) {
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        if (editor == null) return;
        doAction(editor, runner);
    }

    public static void doAction(Editor editor, OCamlConsoleRunner runner) {
        // look for selected code
        String selectedCode = ExtendedEditorActionUtil.getSelectedCode(editor);
        if (selectedCode != null) {
            // send every selected code
            runner.processCommand(selectedCode);
            return;
        }

        // Auto-detect
        ArrayList<PsiElement> autoSelectStatements = ExtendedEditorActionUtil.autoSelectStatements(editor);
        if (autoSelectStatements == null || autoSelectStatements.size() == 0) return;
        // only one, do not ask (and if we are in unit testing too)
        if (autoSelectStatements.size() == 1 || ApplicationManager.getApplication().isUnitTestMode()) {
            runner.processCommand(autoSelectStatements.get(0).getText());
            return;
        }
        // ask which statement?
        List<PsiIntroduceTarget<PsiElement>> targets = autoSelectStatements
                .stream()
                .map(element ->
                        new PsiIntroduceTarget<>(
                                element instanceof PsiLetWithAnd ?
                                        ((PsiLetWithAnd) element).getCore() :
                                        element)
                ).collect(Collectors.toList());
        IntroduceTargetChooser.showIntroduceTargetChooser(editor, targets, new Pass<>() {
            @Override public void pass(PsiIntroduceTarget<PsiElement> choice) {
                // just in case
                if (choice == null || choice.getPlace() == null) return;
                runner.processCommand(choice.getPlace().getText());
            }
        }, OCamlBundle.message("repl.select.a.statement"), 0);
    }
}
