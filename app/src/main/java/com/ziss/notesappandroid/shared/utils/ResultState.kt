package com.ziss.notesappandroid.shared.utils

sealed class ResultState<out R> {
    class Success<T>(val data: T) : ResultState<T>()
    class Failed(val message: String? = null) : ResultState<Nothing>()
    data object Loading : ResultState<Nothing>()
    data object Initial : ResultState<Nothing>()
}