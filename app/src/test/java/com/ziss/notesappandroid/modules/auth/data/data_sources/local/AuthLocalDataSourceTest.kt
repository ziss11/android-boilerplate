package com.ziss.notesappandroid.modules.auth.data.data_sources.local

import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import com.google.gson.Gson
import com.ziss.notesappandroid.dummies.DummyObject.tAuthValidateModel
import com.ziss.notesappandroid.dummies.DummyObject.tTokenModel
import com.ziss.notesappandroid.shared.utils.AppConstants
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AuthLocalDataSourceTest {
    private lateinit var localDataSource: AuthLocalDataSource
    private lateinit var preferences: EncryptedSharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    @Before
    fun setUp() {
        preferences = mock(EncryptedSharedPreferences::class.java)
        editor = mock(SharedPreferences.Editor::class.java)

        `when`(preferences.edit()).thenReturn(editor)

        localDataSource = AuthLocalDataSource(preferences)
    }

    private val accessTokenKey = AppConstants.DataStoreKey.accessToken
    private val refreshTokenKey = AppConstants.DataStoreKey.refreshToken
    private val authSessionKey = AppConstants.DataStoreKey.authSession

    @Test
    fun `setAuthSession - should return true when save auth session is success`() = runTest {
        // Arrange
        val jsonString = Gson().toJson(tAuthValidateModel)
        `when`(editor.putString(authSessionKey, jsonString)).thenReturn(editor)
        `when`(editor.commit()).thenReturn(true)
        // Act
        val result = localDataSource.setAuthSession(tAuthValidateModel)
        // Assert
        assertEquals(result, true)
    }

    @Test
    fun `setAuthSession - should return false when save auth session is failed`() = runTest {
        // Arrange
        `when`(preferences.edit()).thenThrow(RuntimeException())
        // Act
        val result = localDataSource.setAuthSession(tAuthValidateModel)
        // Assert
        assertEquals(result, false)
    }

    @Test
    fun `getAuthSession - should return AuthValidateModel from local when success`() = runTest {
        // Arrange
        val jsonString = Gson().toJson(tAuthValidateModel)
        `when`(preferences.getString(authSessionKey, "")).thenReturn(jsonString)
        // Act
        val result = localDataSource.getAuthSession()
        // Assert
        assertEquals(result, tAuthValidateModel)
    }

    @Test
    fun `getAuthSession - should return null from local when failed`() = runTest {
        // Arrange
        `when`(preferences.getString(authSessionKey, "")).thenReturn("")
        // Act
        val result = localDataSource.getAuthSession()
        // Assert
        assertEquals(result, null)
    }

    @Test
    fun `setToken - should return true when set token is success`() = runTest {
        // Arrange
        `when`(preferences.edit()).thenThrow(RuntimeException())
        // Act
        val result = localDataSource.setToken(tTokenModel)
        // Assert
        assertEquals(result, false)
    }

    @Test
    fun `setToken - should return false when set token is failed`() = runTest {
        // Arrange
        `when`(editor.putString(accessTokenKey, tTokenModel.accessToken)).thenReturn(editor)
        `when`(editor.putString(refreshTokenKey, tTokenModel.refreshToken)).thenReturn(editor)
        `when`(editor.commit()).thenReturn(true)
        // Act
        val result = localDataSource.setToken(tTokenModel)
        // Assert
        assertEquals(result, true)
    }

    @Test
    fun `getAccessToken - should return accessToken from local when success`() = runTest {
        // Arrange
        `when`(preferences.getString(accessTokenKey, null)).thenReturn(tTokenModel.accessToken)
        // Act
        val result = localDataSource.getAccessToken()
        // Assert
        assertEquals(result, tTokenModel.accessToken)
    }

    @Test
    fun `getAccessToken - should return false when failed`() = runTest {
        // Arrange
        `when`(preferences.getString(accessTokenKey, null)).thenReturn(null)
        // Act
        val result = localDataSource.getAccessToken()
        // Assert
        assertEquals(result, null)
    }

    @Test
    fun `getRefreshToken - should return refreshToken from local when success`() = runTest {
        // Arrange
        `when`(preferences.getString(refreshTokenKey, null)).thenReturn(tTokenModel.refreshToken)
        // Act
        val result = localDataSource.getRefreshToken()
        // Assert
        assertEquals(result, tTokenModel.refreshToken)
    }

    @Test
    fun `getRefreshToken - should return false when failed`() = runTest {
        // Arrange
        `when`(preferences.getString(refreshTokenKey, null)).thenReturn(null)
        // Act
        val result = localDataSource.getRefreshToken()
        // Assert
        assertEquals(result, null)
    }

    @Test
    fun `deleteRefreshToken - should return true when delete refreshToken is success`() = runTest {
        // Arrange
        `when`(editor.remove(refreshTokenKey)).thenReturn(editor)
        `when`(editor.commit()).thenReturn(true)
        // Act
        val result = localDataSource.deleteRefreshToken()
        // Assert
        assertEquals(result, true)
    }

    @Test
    fun `deleteRefreshToken - should return false when delete refreshToken is failed`() = runTest {
        // Arrange
        `when`(preferences.edit()).thenThrow(RuntimeException())
        // Act
        val result = localDataSource.deleteRefreshToken()
        // Assert
        assertEquals(result, false)
    }

    @Test
    fun `clearSession - should return true when clear session is success`() = runTest {
        // Arrange
        `when`(editor.remove(accessTokenKey)).thenReturn(editor)
        `when`(editor.remove(refreshTokenKey)).thenReturn(editor)
        `when`(editor.commit()).thenReturn(true)
        // Act
        val result = localDataSource.clearSession()
        // Assert
        assertEquals(result, true)
    }

    @Test
    fun `clearSession - should return false when clear session is failed`() = runTest {
        // Arrange
        `when`(preferences.edit()).thenThrow(RuntimeException())
        // Act
        val result = localDataSource.clearSession()
        // Assert
        assertEquals(result, false)
    }
}

