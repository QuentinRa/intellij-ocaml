package com.dune.ide.typing

import com.dune.language.psi.DuneTypes
import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType

class DuneBraceMatcher : PairedBraceMatcher {
    override fun getPairs() = Constants.PAIRS

    override fun isPairedBracesAllowedBeforeType(lbraceType: IElementType, next: IElementType?): Boolean =
        true
    override fun getCodeConstructStart(file: PsiFile?, openingBraceOffset: Int): Int =
        openingBraceOffset

    internal object Constants {
        val PAIRS: Array<BracePair> = arrayOf(
            BracePair(DuneTypes.LPAREN, DuneTypes.RPAREN, false),
        )
    }
}