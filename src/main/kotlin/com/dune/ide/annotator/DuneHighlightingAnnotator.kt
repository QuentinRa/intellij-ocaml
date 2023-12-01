package com.dune.ide.annotator

import com.dune.ide.colors.DuneColor
import com.dune.language.psi.DuneTypes
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.intellij.psi.util.elementType

class DuneHighlightingAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (holder.isBatchMode) return
        if (element !is LeafPsiElement) return
        val elementType = element.elementType
        if (elementType != DuneTypes.ATOM) return

        // The top-level values are instructions (ex: (top_level ))
        // If they are nested, they are parameters (ex: (top_level (nested)))
        // Otherwise, they are arguments (ex: (top_level argument (nested argument)))
        val color = if (element.prevSibling?.elementType == DuneTypes.LPAREN)
                if (element.parent?.parent is PsiFile) DuneColor.INSTRUCTION
                else DuneColor.ARGUMENT
            else
                DuneColor.PARAMETER

        holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
            .textAttributes(color.textAttributesKey)
            .create()
    }
}