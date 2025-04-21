package com.ziss.notesappandroid.modules.home.presentation.view_models

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import arrow.core.Either.Left
import arrow.core.Either.Right
import com.ziss.notesappandroid.MainDispatcherRule
import com.ziss.notesappandroid.core.common.ServerFailure
import com.ziss.notesappandroid.dummies.DummyObject.tBaseErrorResponse
import com.ziss.notesappandroid.dummies.DummyObject.tNoteModel
import com.ziss.notesappandroid.modules.note.data.models.NoteModel
import com.ziss.notesappandroid.modules.note.data.repositories.NoteRepository
import com.ziss.notesappandroid.shared.utils.ResultState
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
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
class HomeViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: HomeViewModel
    private lateinit var repository: NoteRepository

    @Before
    fun setUp() {
        repository = mock(NoteRepository::class.java)
        viewModel = HomeViewModel(repository)
    }

    @Test
    fun `fetchNotes - state should initial`() = runTest {
        // Assert
        val state = viewModel.notesState.value
        assertTrue(state is ResultState.Initial)
    }

    @Test
    fun `fetchNotes - state should loading when fetchNotes function called`() = runTest {
        // Arrange
        `when`(repository.fetchNotes(refresh = true)).thenAnswer {
            flow {
                delay(1000)
                emit(Right(emptyList<NoteModel>()))
            }
        }
        // Act
        viewModel.fetchNotes(refresh = true)
        // Assert
        val state = viewModel.notesState.value
        println("State: $state")
        assertTrue(state is ResultState.Loading)
    }

    @Test
    fun `fetchNotes - state should initial when fetchNotes is success with empty data`() =
        runTest {
            `when`(repository.fetchNotes(refresh = true)).thenAnswer {
                flowOf(Right(emptyList<NoteModel>()))
            }
            // Act
            viewModel.fetchNotes(refresh = true)
            // Assert
            val state = viewModel.notesState.value
            assertTrue(state is ResultState.Initial)
        }

    @Test
    fun `fetchNotes - state should success when fetchNotes is success with data`() =
        runTest {
            `when`(repository.fetchNotes()).thenAnswer {
                flowOf(Right(listOf(tNoteModel)))
            }
            // Act
            viewModel.fetchNotes()
            // Assert
            val state = viewModel.notesState.value
            assertTrue(state is ResultState.Success)
            assertEquals((state as ResultState.Success).data, listOf(tNoteModel))
        }

    @Test
    fun `fetchNotes - state should failed when fetchNotes is failed`() =
        runTest {
            `when`(repository.fetchNotes()).thenAnswer {
                flowOf(Left(ServerFailure(tBaseErrorResponse)))
            }
            // Act
            viewModel.fetchNotes()
            // Assert
            val state = viewModel.notesState.value
            assertTrue(state is ResultState.Failed)
        }
}