package com.ziss.notesappandroid.core.common

import com.ziss.notesappandroid.shared.responses.BaseErrorResponse

sealed class Failure(
    open val message: String? = null,
    open val error: BaseErrorResponse? = null,
)

data class AuthFailure(
    override val message: String? = null
) : Failure(message, null)

data class ServerFailure(
    override val error: BaseErrorResponse? = null
) : Failure(null, error)
