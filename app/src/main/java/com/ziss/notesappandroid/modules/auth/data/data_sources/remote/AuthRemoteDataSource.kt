package com.ziss.notesappandroid.modules.auth.data.data_sources.remote

import com.ziss.notesappandroid.modules.auth.data.data_sources.remote.services.AuthService
import com.ziss.notesappandroid.modules.auth.data.models.LogoutDto
import com.ziss.notesappandroid.modules.auth.data.models.SignInDto

class AuthRemoteDataSource(private val authService: AuthService) {
    suspend fun signIn(payload: SignInDto) = authService.signIn(payload)

    suspend fun validateAuth() = authService.validateAuth()

    suspend fun logout(payload: LogoutDto) = authService.logout(payload)
}