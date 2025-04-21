package com.ziss.notesappandroid.modules.user.data.data_sources.remote

import com.ziss.notesappandroid.dummies.DummyObject.getResponse
import com.ziss.notesappandroid.dummies.DummyObject.tUserModel
import com.ziss.notesappandroid.modules.user.data.data_sources.remote.services.UserService
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class UserRemoteDataSourceTest {
    private lateinit var service: UserService
    private lateinit var remoteDataSource: UserRemoteDataSource

    @Before
    fun setUp() {
        service = mock(UserService::class.java)
        remoteDataSource = UserRemoteDataSource(service)
    }

    private val tId = 1

    @Test
    fun `getById - should return user when success`() = runTest {
        // Arrange
        `when`(service.getById(tId)).thenAnswer { getResponse(tUserModel) }
        // Act
        val result = remoteDataSource.getById(tId)
        // Assert
        assertEquals(result, getResponse(tUserModel))
    }
}