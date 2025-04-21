package com.ziss.notesappandroid.modules.auth.data.repositories

import arrow.core.Either.Left
import arrow.core.Either.Right
import com.ziss.notesappandroid.core.common.ApiException
import com.ziss.notesappandroid.core.common.AuthFailure
import com.ziss.notesappandroid.core.common.ServerFailure
import com.ziss.notesappandroid.core.common.TokenManager
import com.ziss.notesappandroid.modules.auth.data.data_sources.local.AuthLocalDataSource
import com.ziss.notesappandroid.modules.auth.data.data_sources.remote.AuthRemoteDataSource
import com.ziss.notesappandroid.modules.auth.data.models.LogoutDto
import com.ziss.notesappandroid.modules.auth.data.models.SignInDto
import com.ziss.notesappandroid.modules.user.data.data_sources.remote.UserRemoteDataSource
import kotlinx.coroutines.flow.flow

class AuthRepository(
    private val remoteDataSource: AuthRemoteDataSource,
    private val localDataSource: AuthLocalDataSource,
    private val userRemoteDataSource: UserRemoteDataSource,
    private val tokenManager: TokenManager,
) {
    fun getActiveUser() = flow {
        try {
            val authSession = localDataSource.getAuthSession()
            if (authSession == null) {
                localDataSource.clearSession()
                emit(Left(AuthFailure()))
            }

            val user = userRemoteDataSource.getById(authSession?.id ?: 0)
            emit(Right(user.data))
        } catch (e: ApiException) {
            emit(Left(ServerFailure(e.error)))
        }
    }

    fun validateAuth() = flow {
        try {
            val accessToken = localDataSource.getAccessToken()
            if (accessToken == null) {
                localDataSource.clearSession()
                emit(Left(AuthFailure()))
            }

            if (tokenManager.isTokenExpired(accessToken ?: "")) {
                localDataSource.clearSession()
                emit(Left(AuthFailure()))
            }

            val authData = remoteDataSource.validateAuth()
            if (authData.data == null) {
                localDataSource.clearSession()
                emit(Left(AuthFailure()))
            }

            localDataSource.setAuthSession(authData.data!!)
            emit(Right(authData.data))
        } catch (_: ApiException) {
            localDataSource.clearSession()
            emit(Left(AuthFailure()))
        }
    }

    fun signIn(payload: SignInDto) = flow {
        try {
            val result = remoteDataSource.signIn(payload)
            localDataSource.setToken(result.data!!)
            emit(Right(result.data))
        } catch (e: ApiException) {
            emit(Left(ServerFailure(e.error)))
        }
    }

    fun logout() = flow {
        try {
            val refreshToken = localDataSource.getRefreshToken()
            if (refreshToken == null) {
                localDataSource.clearSession()
                emit(Right(true))
            }

            val payload = LogoutDto(refreshToken ?: "")
            remoteDataSource.logout(payload)

            localDataSource.deleteRefreshToken()
            localDataSource.clearSession()

            emit(Right(true))
        } catch (e: ApiException) {
            emit(Left(ServerFailure(e.error)))
        }
    }
}