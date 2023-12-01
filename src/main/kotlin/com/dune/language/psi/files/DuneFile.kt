package com.dune.language.psi.files

import com.dune.ide.files.DuneFileType
import com.dune.language.DuneLanguage
import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider

class DuneFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, DuneLanguage) {
    override fun getFileType(): FileType = DuneFileType
    override fun toString(): String = "Dune File"
}