package com.ziss.notesappandroid.modules.auth.data.repositories

import arrow.core.Either.Left
import arrow.core.Either.Right
import com.ziss.notesappandroid.core.common.ApiException
import com.ziss.notesappandroid.core.common.AuthFailure
import com.ziss.notesappandroid.core.common.ServerFailure
import com.ziss.notesappandroid.core.common.TokenManager
import com.ziss.notesappandroid.dummies.DummyObject.getResponse
import com.ziss.notesappandroid.dummies.DummyObject.tAuthValidateModel
import com.ziss.notesappandroid.dummies.DummyObject.tBaseErrorResponse
import com.ziss.notesappandroid.dummies.DummyObject.tLogoutDto
import com.ziss.notesappandroid.dummies.DummyObject.tSignInDto
import com.ziss.notesappandroid.dummies.DummyObject.tTokenModel
import com.ziss.notesappandroid.dummies.DummyObject.tUserModel
import com.ziss.notesappandroid.modules.auth.data.data_sources.local.AuthLocalDataSource
import com.ziss.notesappandroid.modules.auth.data.data_sources.remote.AuthRemoteDataSource
import com.ziss.notesappandroid.modules.user.data.data_sources.remote.UserRemoteDataSource
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AuthRepositoryTest {
    private lateinit var remoteDataSource: AuthRemoteDataSource
    private lateinit var localDataSource: AuthLocalDataSource
    private lateinit var userRemoteDataSource: UserRemoteDataSource
    private lateinit var tokenManager: TokenManager
    private lateinit var repository: AuthRepository

    @Before
    fun setUp() {
        remoteDataSource = mock<AuthRemoteDataSource>()
        localDataSource = mock<AuthLocalDataSource>()
        userRemoteDataSource = mock<UserRemoteDataSource>()
        tokenManager = mock<TokenManager>()
        repository = AuthRepository(
            remoteDataSource,
            localDataSource,
            userRemoteDataSource,
            tokenManager
        )
    }

    private val tId = 1
    private val tAccessToken = tTokenModel.accessToken
    private val tRefreshToken = tTokenModel.refreshToken

    @Test
    fun `getActiveUser - should return user when get user data from remote success`() = runTest {
        // Arrange
        `when`(localDataSource.getAuthSession()).thenAnswer {
            tAuthValidateModel
        }
        `when`(userRemoteDataSource.getById(tId)).thenAnswer {
            getResponse(tUserModel)
        }
        // Act
        val result = repository.getActiveUser().first()
        // Assert
        verify(localDataSource).getAuthSession()
        verify(userRemoteDataSource).getById(tId)
        assertEquals(result, Right(tUserModel))
    }

    @Test
    fun `getActiveUser - should return ServerFailure when get user data from remote failed`() =
        runTest {
            // Arrange
            `when`(localDataSource.getAuthSession()).thenAnswer {
                tAuthValidateModel
            }
            `when`(userRemoteDataSource.getById(tId)).thenThrow(
                ApiException(tBaseErrorResponse)
            )
            // Act
            val result = repository.getActiveUser().first()
            // Assert
            verify(localDataSource).getAuthSession()
            verify(userRemoteDataSource).getById(tId)
            verify(localDataSource, times(0)).clearSession()

            assertEquals(result, Left(ServerFailure(tBaseErrorResponse)))
        }

    @Test
    fun `getActiveUser - should return AuthFailure when get auth data from remote failed`() =
        runTest {
            // Arrange
            `when`(localDataSource.getAuthSession()).thenAnswer { null }
            `when`(localDataSource.clearSession()).thenAnswer { true }
            // Act
            val result = repository.getActiveUser().first()
            // Assert
            verify(localDataSource).getAuthSession()
            verify(localDataSource).clearSession()
            verify(userRemoteDataSource, times(0)).getById(tId)

            assertEquals(result, Left(AuthFailure()))
        }

    @Test
    fun `validateAuth - should return current auth data when the call to data source is successful`() =
        runTest {
            // Arrange
            `when`(localDataSource.getAccessToken()).thenAnswer { tAccessToken }
            `when`(tokenManager.isTokenExpired(tAccessToken ?: "")).thenAnswer { false }
            `when`(remoteDataSource.validateAuth()).thenAnswer {
                getResponse(tAuthValidateModel)
            }
            `when`(localDataSource.setAuthSession(tAuthValidateModel)).thenAnswer {
                true
            }
            // Act
            val result = repository.validateAuth().first()
            // Assert
            verify(localDataSource).getAccessToken()
            verify(tokenManager).isTokenExpired(tAccessToken ?: "")
            verify(remoteDataSource).validateAuth()
            verify(localDataSource).setAuthSession(tAuthValidateModel)

            assertEquals(result, Right(tAuthValidateModel))
        }

    @Test
    fun `validateAuth - should return AuthFailure and clear session when access token not saved locally`() =
        runTest {
            // Arrange
            `when`(localDataSource.getAccessToken()).thenAnswer { null }
            `when`(localDataSource.clearSession()).thenAnswer { true }
            // Act
            val result = repository.validateAuth().first()
            // Assert
            verify(localDataSource).getAccessToken()
            verify(localDataSource).clearSession()
            verify(tokenManager, times(0)).isTokenExpired(tAccessToken ?: "")
            verify(remoteDataSource, times(0)).validateAuth()
            verify(localDataSource, times(0)).setAuthSession(tAuthValidateModel)

            assertEquals(result, Left(AuthFailure()))
        }

    @Test
    fun `validateAuth - should return AuthFailure and clear session when token is expired`() =
        runTest {
            // Arrange
            `when`(localDataSource.getAccessToken()).thenAnswer { tAccessToken }
            `when`(tokenManager.isTokenExpired(tAccessToken ?: "")).thenAnswer { true }
            `when`(localDataSource.clearSession()).thenAnswer { true }
            // Act
            val result = repository.validateAuth().first()
            // Assert
            verify(localDataSource).getAccessToken()
            verify(tokenManager).isTokenExpired(tAccessToken ?: "")
            verify(localDataSource).clearSession()

            assertEquals(result, Left(AuthFailure()))
        }

    @Test
    fun `validateAuth - should return AuthFailure and clear session when get auth data from remote is success but the data is null`() =
        runTest {
            // Arrange
            `when`(localDataSource.getAccessToken()).thenAnswer {
                tAccessToken
            }
            `when`(tokenManager.isTokenExpired(tAccessToken ?: "")).thenAnswer { false }
            `when`(remoteDataSource.validateAuth()).thenAnswer {
                getResponse(null)
            }
            `when`(localDataSource.clearSession()).thenAnswer {
                true
            }
            // Act
            val result = repository.validateAuth().first()
            // Assert
            verify(localDataSource).getAccessToken()
            verify(tokenManager).isTokenExpired(tAccessToken ?: "")
            verify(remoteDataSource).validateAuth()
            verify(localDataSource).clearSession()
            verify(localDataSource, times(0)).setAuthSession(tAuthValidateModel)

            assertEquals(result, Left(AuthFailure()))
        }

    @Test
    fun `validateAuth - should return AuthFailure and clear session when get auth data from remote is failed`() =
        runTest {
            // Arrange
            `when`(localDataSource.getAccessToken()).thenAnswer {
                tAccessToken
            }
            `when`(tokenManager.isTokenExpired(tAccessToken ?: "")).thenAnswer { false }
            `when`(remoteDataSource.validateAuth()).thenThrow(
                ApiException(tBaseErrorResponse)
            )
            `when`(localDataSource.clearSession()).thenAnswer {
                true
            }
            // Act
            val result = repository.validateAuth().first()
            // Assert
            verify(localDataSource).getAccessToken()
            verify(tokenManager).isTokenExpired(tAccessToken ?: "")
            verify(remoteDataSource).validateAuth()
            verify(localDataSource).clearSession()
            verify(localDataSource, times(0)).setAuthSession(tAuthValidateModel)

            assertEquals(result, Left(AuthFailure()))
        }

    @Test
    fun `signIn - should return tokens and save token to local when signIn is success`() = runTest {
        // Arrange
        `when`(remoteDataSource.signIn(tSignInDto)).thenAnswer {
            getResponse(tTokenModel)
        }
        `when`(localDataSource.setToken(tTokenModel)).thenAnswer {
            true
        }
        // Act
        val result = repository.signIn(tSignInDto).first()
        // Assert
        verify(remoteDataSource).signIn(tSignInDto)
        verify(localDataSource).setToken(tTokenModel)

        assertEquals(result, Right(tTokenModel))
    }

    @Test
    fun `signIn - should return server failure when signIn is failed`() = runTest {
        // Arrange
        `when`(remoteDataSource.signIn(tSignInDto)).thenThrow(
            ApiException(tBaseErrorResponse)
        )
        // Act
        val result = repository.signIn(tSignInDto).first()
        // Assert
        verify(remoteDataSource).signIn(tSignInDto)
        verify(localDataSource, times(0)).setToken(tTokenModel)

        assertEquals(result, Left(ServerFailure(tBaseErrorResponse)))
    }

    @Test
    fun `logout - should return true and clear session when refresh token is null`() = runTest {
        // Arrange
        `when`(localDataSource.getRefreshToken()).thenAnswer { null }
        `when`(localDataSource.clearSession()).thenAnswer { true }
        // Act
        val result = repository.logout().first()
        // Assert
        verify(localDataSource).getRefreshToken()
        verify(localDataSource).clearSession()

        assertEquals(result, Right(true))
    }

    @Test
    fun `logout - should return true and clear session when logout is success`() = runTest {
        // Arrange
        `when`(localDataSource.getRefreshToken()).thenAnswer {
            tRefreshToken
        }
        `when`(remoteDataSource.logout(tLogoutDto)).thenAnswer {
            getResponse(Unit)
        }
        `when`(localDataSource.deleteRefreshToken()).thenAnswer { true }
        `when`(localDataSource.clearSession()).thenAnswer { true }
        // Act
        val result = repository.logout().first()
        // Assert
        verify(localDataSource).getRefreshToken()
        verify(remoteDataSource).logout(tLogoutDto)
        verify(localDataSource).deleteRefreshToken()
        verify(localDataSource).clearSession()

        assertEquals(result, Right(true))
    }

    @Test
    fun `logout - should return ServerFailure when logout is failed`() = runTest {
        // Arrange
        `when`(localDataSource.getRefreshToken()).thenAnswer {
            tRefreshToken
        }
        `when`(remoteDataSource.logout(tLogoutDto)).thenThrow(
            ApiException(tBaseErrorResponse)
        )
        // Act
        val result = repository.logout().first()
        // Assert
        verify(localDataSource).getRefreshToken()
        verify(remoteDataSource).logout(tLogoutDto)

        assertEquals(result, Left(ServerFailure(tBaseErrorResponse)))
    }
}