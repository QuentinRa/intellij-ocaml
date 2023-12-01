package com.dune.ide.commenter

import com.intellij.lang.Commenter

class DuneCommenter : Commenter {
    override fun getLineCommentPrefix(): String = ";"
    override fun getBlockCommentPrefix(): String  = ";"
    override fun getBlockCommentSuffix(): String  = ";"
    override fun getCommentedBlockCommentPrefix(): String  = ";"
    override fun getCommentedBlockCommentSuffix(): String  = ";"
}