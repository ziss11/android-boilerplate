package com.ziss.notesappandroid.modules.user.data.data_sources.remote.services

import com.ziss.notesappandroid.modules.user.data.models.UserModel
import com.ziss.notesappandroid.shared.responses.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Path


interface UserService {
    @GET("/users/{id}")
    suspend fun getById(@Path("id") id: Int): BaseResponse<UserModel>
}