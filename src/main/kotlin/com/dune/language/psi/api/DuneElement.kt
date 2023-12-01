package com.dune.language.psi.api

import com.intellij.openapi.util.UserDataHolderEx
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNameIdentifierOwner
import com.intellij.psi.PsiNamedElement

interface DuneElement : PsiElement, UserDataHolderEx
interface DuneNamedElement : DuneElement, PsiNamedElement
interface DuneNamedOwnerElement : DuneNamedElement, PsiNameIdentifierOwner