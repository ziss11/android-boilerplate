package com.ziss.notesappandroid.shared.responses

data class BaseErrorResponse(
    val type: String?,
    val errors: List<ErrorDetailResponse>?,
)
