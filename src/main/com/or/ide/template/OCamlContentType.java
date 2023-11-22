package com.or.ide.template;

import com.intellij.codeInsight.template.TemplateActionContext;
import com.intellij.codeInsight.template.TemplateContextType;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiUtilCore;
import com.ocaml.OCamlLanguage;
import org.jetbrains.annotations.NotNull;

/**
 * Every "*.ml" kind of file is considered to be a part of the OCaml context
 */
public class OCamlContentType extends TemplateContextType {

    @SuppressWarnings("unused") protected OCamlContentType() {
        super("OCAML", "Ocaml");
    }

    protected OCamlContentType(@NotNull String name) {
        super("OCAML." + name, name, OCamlContentType.class);
    }

    @Override public boolean isInContext(@NotNull TemplateActionContext templateActionContext) {
        final String name = templateActionContext.getFile().getName();
        return name.endsWith(".ml") || name.endsWith(".mli");
    }

    // Code-only
    @SuppressWarnings("unused")
    private static final class OCamlExpressionTemplates extends ScopeTemplates {
        public OCamlExpressionTemplates() {
            super("Expression", false);
        }
    }

    // Comments only
    @SuppressWarnings("unused")
    private static final class OCamlCommentTemplates extends ScopeTemplates {
        public OCamlCommentTemplates() {
            super("Comment", true);
        }
    }

    private static class ScopeTemplates extends OCamlContentType {
        private final boolean myOnComment;

        /**
         * @param name      name of the scope, capitalized
         * @param onComment if true, then "isContext" is checking that we are inside a Comment,
         *                  otherwise, "isContext" is checking that we aren't inside a Comment
         */
        protected ScopeTemplates(@NotNull String name, boolean onComment) {
            super(name);
            myOnComment = onComment;
        }

        @Override public boolean isInContext(@NotNull TemplateActionContext templateActionContext) {
            if (!super.isInContext(templateActionContext)) {
                return false;
            }

            // RmlContextType - copy of the 6 following lines
            // with RmlLanguage => OclLanguage
            PsiFile file = templateActionContext.getFile();
            int offset = templateActionContext.getStartOffset();
            if (!PsiUtilCore.getLanguageAtOffset(file, offset).isKindOf(OCamlLanguage.INSTANCE)) {
                return false;
            }

            PsiElement element = file.findElementAt(offset);
            return myOnComment == (element instanceof PsiComment);
        }
    }
}
