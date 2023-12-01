package com.dune.ide.files

import com.dune.DuneBundle
import com.dune.icons.DuneIcons
import com.dune.language.DuneLanguage
import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

object DuneFileType : LanguageFileType(DuneLanguage) {
    // LanguageFileType implementation
    override fun getName(): String  = "Dune File"
    override fun getDescription(): String = DuneBundle.message("filetype.dune.description")
    override fun getDefaultExtension(): String = ""
    override fun getIcon(): Icon = DuneIcons.FileTypes.DUNE_FILE
}