package com.ziss.notesappandroid.modules.auth.data.data_sources.remote

import com.ziss.notesappandroid.dummies.DummyObject.getResponse
import com.ziss.notesappandroid.dummies.DummyObject.tAuthValidateModel
import com.ziss.notesappandroid.dummies.DummyObject.tLogoutDto
import com.ziss.notesappandroid.dummies.DummyObject.tSignInDto
import com.ziss.notesappandroid.dummies.DummyObject.tTokenModel
import com.ziss.notesappandroid.modules.auth.data.data_sources.remote.services.AuthService
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AuthRemoteDataSourceTest {
    private lateinit var authService: AuthService
    private lateinit var remoteDataSource: AuthRemoteDataSource

    @Before
    fun setUp() {
        authService = mock<AuthService>()
        remoteDataSource = AuthRemoteDataSource(authService)
    }

    @Test
    fun `signIn - should return access token and refresh token when success`() = runTest {
        // Arrange
        `when`(authService.signIn(tSignInDto)).thenAnswer {
            getResponse(tTokenModel)
        }
        // Act
        val result = remoteDataSource.signIn(tSignInDto)
        // Assert
        assertEquals(result, getResponse(tTokenModel))
    }

    @Test
    fun `validateAuth - should return auth data when success`() = runTest {
        // Arrange
        `when`(authService.validateAuth()).thenAnswer {
            getResponse(tAuthValidateModel)
        }
        // Act
        val result = remoteDataSource.validateAuth()
        // Assert
        assertEquals(result, getResponse(tAuthValidateModel))
    }

    @Test
    fun `logout - should call logout function when success`() = runTest {
        // Arrange
        `when`(authService.logout(tLogoutDto)).thenAnswer {
            getResponse(Unit)
        }
        // Act
        remoteDataSource.logout(tLogoutDto)
        // Assert
        verify(authService).logout(tLogoutDto)
    }
}