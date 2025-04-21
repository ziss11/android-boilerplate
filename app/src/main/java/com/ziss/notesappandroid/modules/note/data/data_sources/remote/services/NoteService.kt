package com.ziss.notesappandroid.modules.note.data.data_sources.remote.services

import com.ziss.notesappandroid.modules.note.data.models.NoteDto
import com.ziss.notesappandroid.modules.note.data.models.NoteModel
import com.ziss.notesappandroid.shared.responses.BaseResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface NoteService {
    @POST("/notes")
    suspend fun create(@Body note: NoteDto): BaseResponse<NoteModel>

    @DELETE("/notes/{id}")
    suspend fun delete(@Path("id") id: Int)

    @GET("/notes")
    suspend fun fetch(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): BaseResponse<List<NoteModel>>

    @PATCH("/notes/{id}")
    suspend fun update(@Path("id") id: Int, @Body note: NoteDto): BaseResponse<NoteModel>

    @GET("/notes/{id}")
    suspend fun getById(@Path("id") id: Int): BaseResponse<NoteModel>
}