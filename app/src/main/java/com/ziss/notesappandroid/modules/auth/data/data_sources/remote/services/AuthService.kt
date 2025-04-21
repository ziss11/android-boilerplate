package com.ziss.notesappandroid.modules.auth.data.data_sources.remote.services

import androidx.room.Dao
import com.ziss.notesappandroid.modules.auth.data.models.AuthValidateModel
import com.ziss.notesappandroid.modules.auth.data.models.LogoutDto
import com.ziss.notesappandroid.modules.auth.data.models.SignInDto
import com.ziss.notesappandroid.modules.auth.data.models.TokenModel
import com.ziss.notesappandroid.shared.responses.BaseResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

@Dao
interface AuthService {
    @POST("/auth/login")
    suspend fun signIn(@Body payload: SignInDto): BaseResponse<TokenModel>

    @GET("/auth/validate")
    suspend fun validateAuth(): BaseResponse<AuthValidateModel>

    @POST("/auth/logout")
    suspend fun logout(@Body payload: LogoutDto): BaseResponse<Unit>
}