package com.ocaml.language.psi.api

interface OCamlLetDeclaration {
    fun isFunction() : Boolean

    /**
     * Is this variable or function directly inside the file (global=true)
     * or it is inside a module/class/another let statement/etc. (global=false)?
     */
    fun isGlobal() : Boolean
}