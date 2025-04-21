package com.ziss.notesappandroid.core.common

import com.ziss.notesappandroid.shared.responses.BaseErrorResponse

open class ApiException(open val error: BaseErrorResponse?) : RuntimeException()

open class AuthException(override val message: String?) : RuntimeException()

open class DatabaseException(override val message: String?) : RuntimeException()

