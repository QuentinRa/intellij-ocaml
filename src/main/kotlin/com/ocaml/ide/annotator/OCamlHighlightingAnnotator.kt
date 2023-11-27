package com.ocaml.ide.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.ocaml.ide.colors.OCamlColor
import com.ocaml.language.psi.OCamlTypes
import com.ocaml.language.psi.api.OCamlLetDeclaration
import com.ocaml.language.psi.api.OCamlNameIdentifierOwner

class OCamlHighlightingAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (holder.isBatchMode) return
        if (element !is LeafPsiElement) return
        val elementType = element.elementType

        // OCamlNameIdentifierOwner
        // todo: only works with "let", not generic at all (and a tad hard-coded)
        if (elementType != OCamlTypes.LOWERCASE_IDENT_VALUE) return
        val ancestor = element.parent // LOWERCASE_IDENT
            ?.parent // VALUE_NAME
            ?.parent // OCamlNameIdentifierOwner
        if (ancestor !is OCamlNameIdentifierOwner) return
        val color =
            if ((ancestor as? OCamlLetDeclaration)?.isFunction() == true) OCamlColor.FUNCTION_DECLARATION
            else if (ancestor.parent is PsiFile) OCamlColor.GLOBAL_VARIABLE // VAL
            else if (ancestor.parent.parent is PsiFile) OCamlColor.GLOBAL_VARIABLE // LET
            else OCamlColor.LOCAL_VARIABLE
        holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
            .textAttributes(color.textAttributesKey)
            .create()
    }
}