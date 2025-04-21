package com.ziss.notesappandroid.core.extensions

fun String.capitalize(): String {
    return this.replaceFirstChar { char -> uppercase() }
}