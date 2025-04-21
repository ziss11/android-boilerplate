package com.ziss.notesappandroid.core.interceptors

import androidx.security.crypto.EncryptedSharedPreferences
import com.ziss.notesappandroid.shared.utils.AppConstants
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor(private val preferences: EncryptedSharedPreferences) :
    Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val tokenKey = AppConstants.DataStoreKey.accessToken
        val accessToken = preferences.getString(tokenKey, "")

        val newRequest = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $accessToken")
            .build()

        return chain.proceed(newRequest)
    }
}