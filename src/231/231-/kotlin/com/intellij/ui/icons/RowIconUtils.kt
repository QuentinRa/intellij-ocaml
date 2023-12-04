package com.intellij.ui.icons

import javax.swing.Icon

@Suppress("UnstableApiUsage")
fun Icon?.allIconsStable(): List<Icon>? {
    return (this as? RowIcon)?.allIcons?.asList()
}