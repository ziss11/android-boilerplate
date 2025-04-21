package com.ziss.notesappandroid.modules.auth.data.data_sources.local

import androidx.security.crypto.EncryptedSharedPreferences
import com.google.gson.Gson
import com.ziss.notesappandroid.modules.auth.data.models.AuthValidateModel
import com.ziss.notesappandroid.modules.auth.data.models.TokenModel
import com.ziss.notesappandroid.shared.utils.AppConstants

class AuthLocalDataSource(private val preferences: EncryptedSharedPreferences) {
    fun setAuthSession(auth: AuthValidateModel): Boolean {
        try {
            val key = AppConstants.DataStoreKey.authSession
            val data = Gson().toJson(auth)
            return preferences.edit().putString(key, data).commit()
        } catch (_: Exception) {
            return false
        }
    }

    fun getAuthSession(): AuthValidateModel? {
        val key = AppConstants.DataStoreKey.authSession
        val response = preferences.getString(key, "")
        return Gson().fromJson(response, AuthValidateModel::class.java)
    }

    fun setToken(tokens: TokenModel): Boolean {
        try {
            val accessTokenKey = AppConstants.DataStoreKey.accessToken
            val refreshTokenKey = AppConstants.DataStoreKey.refreshToken
            return preferences.edit()
                .putString(accessTokenKey, tokens.accessToken)
                .putString(refreshTokenKey, tokens.refreshToken)
                .commit()
        } catch (_: Exception) {
            return false
        }
    }

    fun getAccessToken(): String? {
        val key = AppConstants.DataStoreKey.accessToken
        return preferences.getString(key, null)
    }

    fun getRefreshToken(): String? {
        val key = AppConstants.DataStoreKey.refreshToken
        return preferences.getString(key, null)
    }

    fun deleteRefreshToken(): Boolean {
        try {
            val key = AppConstants.DataStoreKey.refreshToken
            return preferences.edit().remove(key).commit()
        } catch (_: Exception) {
            return false
        }
    }

    fun clearSession(): Boolean {
        try {
            val accessTokenKey = AppConstants.DataStoreKey.accessToken
            val refreshTokenKey = AppConstants.DataStoreKey.refreshToken
            return preferences.edit()
                .remove(accessTokenKey)
                .remove(refreshTokenKey)
                .commit()
        } catch (_: Exception) {
            return false
        }
    }
}