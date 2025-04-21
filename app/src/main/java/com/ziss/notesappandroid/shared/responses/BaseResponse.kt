package com.ziss.notesappandroid.shared.responses

data class BaseResponse<T>(
    val data: T?,
    val meta: MetaResponse?,
)
