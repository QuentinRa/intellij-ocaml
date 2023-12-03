package com.intellij.ui.icons

import javax.swing.Icon

// See also: "import com.intellij.ui.RowIcon"
// Find a way to get around this
@Suppress("UnstableApiUsage")
fun Icon?.allIconsStable(): Array<out Icon>? {
    return (this as? RowIcon)?.allIcons
}