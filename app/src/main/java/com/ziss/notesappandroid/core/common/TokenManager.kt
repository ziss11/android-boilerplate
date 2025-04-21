package com.ziss.notesappandroid.core.common

import com.auth0.android.jwt.JWT
import java.util.Date

class TokenManager {
    fun isTokenExpired(token: String): Boolean {
        val jwt = JWT(token)
        val expiresAt = jwt.expiresAt
        return expiresAt != null && expiresAt.before(Date())
    }
}