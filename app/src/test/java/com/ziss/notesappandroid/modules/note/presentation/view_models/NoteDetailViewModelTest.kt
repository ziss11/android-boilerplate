package com.ziss.notesappandroid.modules.note.presentation.view_models

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import arrow.core.Either.Left
import arrow.core.Either.Right
import com.ziss.notesappandroid.MainDispatcherRule
import com.ziss.notesappandroid.core.common.ServerFailure
import com.ziss.notesappandroid.dummies.DummyObject.getResponse
import com.ziss.notesappandroid.dummies.DummyObject.tBaseErrorResponse
import com.ziss.notesappandroid.dummies.DummyObject.tNoteModel
import com.ziss.notesappandroid.modules.note.data.repositories.NoteRepository
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
class NoteDetailViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: NoteDetailViewModel
    private lateinit var repository: NoteRepository

    @Before
    fun setUp() {
        repository = mock(NoteRepository::class.java)
        viewModel = NoteDetailViewModel(repository)
    }

    private val tId = 1

    @Test
    fun `getNoteById - state should loading when calling getNoteById`() = runTest {
        // Arrange
        `when`(repository.getNoteById(tId)).thenAnswer {
            flowOf(Right(getResponse(tNoteModel)))
        }
        // Act
        val result = viewModel.getNoteById(tId).getOrAwaitValue()
        // Assert
        assertTrue(result is ResultState.Loading)
    }

    @Test
    fun `getNoteById - state should success when get note by id success`() = runTest {
        // Arrange
        `when`(repository.getNoteById(tId)).thenAnswer {
            flowOf(Right(getResponse(tNoteModel)))
        }
        // Act
        val result = viewModel.getNoteById(tId)
        // Assert
        result.observeForTesting {
            assertTrue(result.value is ResultState.Success)
            assertEquals((result.value as ResultState.Success).data, tNoteModel)
        }
    }

    @Test
    fun `getNoteById - state should failed when get note by id failed`() = runTest {
        // Arrange
        `when`(repository.getNoteById(tId)).thenAnswer {
            flowOf(Left(ServerFailure(tBaseErrorResponse)))
        }
        // Act
        val result = viewModel.getNoteById(tId)
        // Assert
        result.observeForTesting {
            assertTrue(result.value is ResultState.Failed)
        }
    }

    @Test
    fun `deleteNote - state should loading when deleteNote called`() = runTest {
        // Arrange
        `when`(repository.deleteNote(tId)).thenAnswer {
            flowOf(Right("Note Deleted"))
        }
        // Act
        val result = viewModel.deleteNote(tId).getOrAwaitValue()
        // Assert
        assertTrue(result is ResultState.Loading)
    }

    @Test
    fun `deleteNote - state should success when deleteNote is success`() = runTest {
        // Arrange
        `when`(repository.deleteNote(tId)).thenAnswer {
            flowOf(Right("Note Deleted"))
        }
        // Act
        val result = viewModel.deleteNote(tId)
        // Assert
        result.observeForTesting {
            assertTrue(result.value is ResultState.Success)
            assertEquals((result.value as ResultState.Success).data, "Note Deleted")
        }
    }

    @Test
    fun `deleteNote - state should failed when deleteNote is failed`() = runTest {
        // Arrange
        `when`(repository.deleteNote(tId)).thenAnswer {
            flowOf(Left(ServerFailure(tBaseErrorResponse)))
        }
        // Act
        val result = viewModel.deleteNote(tId)
        // Assert
        result.observeForTesting {
            assertTrue(result.value is ResultState.Failed)
        }
    }
}