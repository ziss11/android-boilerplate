package com.ziss.notesappandroid.core.interceptors

import com.google.gson.Gson
import com.ziss.notesappandroid.core.common.ApiException
import com.ziss.notesappandroid.shared.responses.BaseErrorResponse
import com.ziss.notesappandroid.shared.responses.ErrorDetailResponse
import okhttp3.Interceptor
import okhttp3.Response

class ErrorInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        if (!response.isSuccessful) {
            val errorBody = response.body?.string()
            val baseErrorResponse = try {
                Gson().fromJson(errorBody, BaseErrorResponse::class.java)
            } catch (e: Exception) {
                BaseErrorResponse(
                    "Unknown error",
                    listOf(
                        ErrorDetailResponse(
                            "Unknown error",
                            ""
                        )
                    )
                )
            }

            throw ApiException(baseErrorResponse)
        }

        return response
    }
}