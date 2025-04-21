package com.ziss.notesappandroid.modules.auth.presentation.view_models

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import arrow.core.Either.Left
import arrow.core.Either.Right
import com.ziss.notesappandroid.MainDispatcherRule
import com.ziss.notesappandroid.core.common.ServerFailure
import com.ziss.notesappandroid.dummies.DummyObject.tBaseErrorResponse
import com.ziss.notesappandroid.dummies.DummyObject.tSignInDto
import com.ziss.notesappandroid.dummies.DummyObject.tTokenModel
import com.ziss.notesappandroid.modules.auth.data.repositories.AuthRepository
import com.ziss.notesappandroid.shared.utils.ResultState
import com.ziss.notesappandroid.utils.getOrAwaitValue
import com.ziss.notesappandroid.utils.observeForTesting
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class SignInViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: SignInViewModel
    private lateinit var repository: AuthRepository

    @Before
    fun setUp() {
        repository = mock(AuthRepository::class.java)
        viewModel = SignInViewModel(repository)
    }

    @Test
    fun `signIn - state should loading when signIn function called`() = runTest {
        // Arrange
        `when`(repository.signIn(tSignInDto)).thenAnswer {
            flowOf(Right(tTokenModel))
        }
        // Act
        val result = viewModel.signIn(tSignInDto).getOrAwaitValue()
        // Assert
        assertTrue(result is ResultState.Loading)
    }

    @Test
    fun `signIn - state should success when signIn is successful`() = runTest {
        // Arrange
        `when`(repository.signIn(tSignInDto)).thenAnswer {
            flowOf(Right(tTokenModel))
        }
        // Act
        val result = viewModel.signIn(tSignInDto)
        // Assert
        result.observeForTesting {
            assertTrue(result.value is ResultState.Success)
            assertEquals((result.value as ResultState.Success).data, tTokenModel)
        }
    }

    @Test
    fun `signIn - state should failed when signIn is unsuccessful`() = runTest {
        // Arrange
        `when`(repository.signIn(tSignInDto)).thenAnswer {
            flowOf(Left(ServerFailure(tBaseErrorResponse)))
        }
        // Act
        val result = viewModel.signIn(tSignInDto)
        // Assert
        result.observeForTesting {
            assertTrue(result.value is ResultState.Failed)
        }
    }
}