package com.reason.lang.core;

import com.intellij.lang.Language;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFileFactory;
import com.reason.lang.ocaml.*;
import com.reason.ide.files.FileBase;
import com.reason.lang.core.psi.PsiInnerModule;
import com.reason.lang.core.psi.PsiLet;
import com.reason.lang.core.psi.impl.PsiLowerIdentifier;
import com.reason.lang.core.psi.impl.PsiUpperIdentifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ORCodeFactory {
  private ORCodeFactory() {}

  @Nullable
  public static PsiUpperIdentifier createModuleName(@NotNull Project project, @NotNull String name) {
    FileBase file = createFileFromText(project, OclLanguage.INSTANCE, "module " + name + " = {};");
    PsiInnerModule module = ORUtil.findImmediateFirstChildOfClass(file, PsiInnerModule.class);
    return ORUtil.findImmediateFirstChildOfClass(module, PsiUpperIdentifier.class);
  }

  @Nullable
  public static PsiLowerIdentifier createLetName(@NotNull Project project, @NotNull String name) {
    FileBase file = createFileFromText(project, OclLanguage.INSTANCE, "let " + name + " = 1;");
    PsiLet let = ORUtil.findImmediateFirstChildOfClass(file, PsiLet.class);
    return ORUtil.findImmediateFirstChildOfClass(let, PsiLowerIdentifier.class);
  }

  @NotNull
  public static FileBase createFileFromText(
      @NotNull Project project, @NotNull Language language, @NotNull String text) {
    return (FileBase)
        PsiFileFactory.getInstance(project).createFileFromText("Dummy", language, text);
  }
}
