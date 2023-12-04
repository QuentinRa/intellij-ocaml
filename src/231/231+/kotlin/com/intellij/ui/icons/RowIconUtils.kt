package com.intellij.ui.icons

import javax.swing.Icon

fun Icon?.allIconsStable(): List<Icon>? {
    return (this as? RowIcon)?.allIcons
}