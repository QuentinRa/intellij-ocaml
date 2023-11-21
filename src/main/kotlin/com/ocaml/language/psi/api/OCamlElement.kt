package com.ocaml.language.psi.api

import com.intellij.openapi.util.UserDataHolderEx
import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNameIdentifierOwner
import com.intellij.psi.PsiNamedElement

// an element
interface OCamlElement : PsiElement, UserDataHolderEx

// an element with a name
interface OCamlNamedElement : OCamlElement, PsiNamedElement, NavigatablePsiElement

// an element with a name given from another element
interface OCamlNameIdentifierOwner : OCamlNamedElement, PsiNameIdentifierOwner