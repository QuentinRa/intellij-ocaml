package com.ocaml.ide.template

import com.intellij.codeInsight.template.TemplateActionContext
import com.intellij.codeInsight.template.TemplateContextTypePatch
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiUtilCore
import com.ocaml.OCamlBundle
import com.ocaml.ide.files.OCamlLanguage
import com.ocaml.ide.highlight.OCamlSyntaxHighlighter

// Don't forget to update TemplateContextTypePatch for older versions
sealed class OCamlContextType(presentableName: String) : TemplateContextTypePatch(presentableName) {

    final override fun isInContext(context: TemplateActionContext): Boolean {
        if (!PsiUtilCore.getLanguageAtOffset(context.file, context.startOffset).isKindOf(OCamlLanguage)) {
            return false
        }

        val element = context.file.findElementAt(context.startOffset)
        if (element == null || element is PsiComment) {
            return false
        }

        return isInContext(element)
    }

    protected abstract fun isInContext(element: PsiElement): Boolean

    override fun createHighlighter(): SyntaxHighlighter? = OCamlSyntaxHighlighter()

    class Generic : OCamlContextType(OCamlBundle.message("language.name")) {
        override fun isInContext(element: PsiElement): Boolean = true
    }
}