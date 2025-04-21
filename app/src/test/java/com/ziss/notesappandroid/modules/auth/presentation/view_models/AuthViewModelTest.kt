package com.ziss.notesappandroid.modules.auth.presentation.view_models

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import arrow.core.Either.Left
import arrow.core.Either.Right
import com.ziss.notesappandroid.MainDispatcherRule
import com.ziss.notesappandroid.core.common.AuthFailure
import com.ziss.notesappandroid.dummies.DummyObject.tAuthValidateModel
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
class AuthViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: AuthViewModel
    private lateinit var repository: AuthRepository

    @Before
    fun setUp() {
        repository = mock(AuthRepository::class.java)
        viewModel = AuthViewModel(repository)
    }

    @Test
    fun `authCheck - state should loading when authCheck function called`() = runTest {
        // Arrange
        `when`(repository.validateAuth()).thenAnswer {
            flowOf(Right(tAuthValidateModel))
        }
        // Act
        val result = viewModel.authCheck().getOrAwaitValue()
        // Assert
        assertTrue(result is ResultState.Loading)
    }

    @Test
    fun `authCheck - state should success if user is already logged in`() = runTest {
        // Arrange
        `when`(repository.validateAuth()).thenAnswer {
            flowOf(Right(tAuthValidateModel))
        }
        // Act
        val result = viewModel.authCheck()
        // Assert
        result.observeForTesting {
            assertTrue(result.value is ResultState.Success)
            assertEquals((result.value as ResultState.Success).data, tAuthValidateModel)
        }
    }

    @Test
    fun `authCheck - state should failed if user is not logged in`() = runTest {
        // Arrange
        `when`(repository.validateAuth()).thenAnswer {
            flowOf(Left(AuthFailure()))
        }
        // Act
        val result = viewModel.authCheck()
        // Assert
        result.observeForTesting {
            assertTrue(result.value is ResultState.Failed)
        }
    }
}