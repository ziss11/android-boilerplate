package com.ziss.notesappandroid.core.extensions

fun <T> List<T>.toArrayList(): ArrayList<T> {
    return toCollection(ArrayList<T>())
}