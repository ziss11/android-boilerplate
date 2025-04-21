package com.ziss.notesappandroid.modules.note.presentation.view_models

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import arrow.core.Either.Left
import arrow.core.Either.Right
import com.ziss.notesappandroid.MainDispatcherRule
import com.ziss.notesappandroid.core.common.ServerFailure
import com.ziss.notesappandroid.dummies.DummyObject.getResponse
import com.ziss.notesappandroid.dummies.DummyObject.tBaseErrorResponse
import com.ziss.notesappandroid.dummies.DummyObject.tNoteDto
import com.ziss.notesappandroid.dummies.DummyObject.tNoteModel
import com.ziss.notesappandroid.modules.note.data.repositories.NoteRepository
import com.ziss.notesappandroid.shared.utils.ResultState
import com.ziss.notesappandroid.utils.getOrAwaitValue
import com.ziss.notesappandroid.utils.observeForTesting
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class NoteFormViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: NoteFormViewModel
    private lateinit var repository: NoteRepository

    @Before
    fun setUp() {
        repository = mock(NoteRepository::class.java)
        viewModel = NoteFormViewModel(repository)
    }

    private val tId = 1

    @Test
    fun `saveNote - state should loading when saveNote called and id is null`() = runTest {
        // Arrange
        `when`(repository.createNote(tNoteDto)).thenAnswer {
            MutableStateFlow(Right(getResponse(tNoteModel)))
        }
        // Act
        val result = viewModel.saveNote(tNoteDto).getOrAwaitValue()
        // Assert
        assertTrue(result is ResultState.Loading)
    }

    @Test
    fun `saveNote - state should loading when saveNote called and id is not null`() = runTest {
        // Arrange
        `when`(repository.updateNote(tId, tNoteDto)).thenAnswer {
            MutableStateFlow(Right(getResponse(tNoteModel)))
        }
        // Act
        val result = viewModel.saveNote(tNoteDto, tId).getOrAwaitValue()
        // Assert
        assertTrue(result is ResultState.Loading)
    }

    @Test
    fun `saveNote - state should success and call createNote when success`() = runTest {
        // Arrange
        `when`(repository.createNote(tNoteDto)).thenAnswer {
            MutableStateFlow(Right(getResponse(tNoteModel)))
        }
        // Act
        val result = viewModel.saveNote(tNoteDto)
        // Assert
        result.observeForTesting {
            verify(repository).createNote(tNoteDto)
            verify(repository, times(0)).updateNote(tId, tNoteDto)

            assertTrue(result.value is ResultState.Success)
            assertEquals((result.value as ResultState.Success).data.data, tNoteModel)
        }
    }

    @Test
    fun `saveNote - state should success and call updateNote when success`() = runTest {
        // Arrange
        `when`(repository.updateNote(tId, tNoteDto)).thenAnswer {
            MutableStateFlow(Right(getResponse(tNoteModel)))
        }
        // Act
        val result = viewModel.saveNote(tNoteDto, tId)
        // Assert
        result.observeForTesting {
            verify(repository).updateNote(tId, tNoteDto)
            verify(repository, times(0)).createNote(tNoteDto)

            assertTrue(result.value is ResultState.Success)
            assertEquals((result.value as ResultState.Success).data.data, tNoteModel)
        }
    }

    @Test
    fun `saveNote - state should failed when createNote is failed`() = runTest {
        // Arrange
        `when`(repository.createNote(tNoteDto)).thenAnswer {
            MutableStateFlow(Left(ServerFailure(tBaseErrorResponse)))
        }
        // Act
        val result = viewModel.saveNote(tNoteDto)
        // Assert
        result.observeForTesting {
            verify(repository).createNote(tNoteDto)
            verify(repository, times(0)).updateNote(tId, tNoteDto)

            assertTrue(result.value is ResultState.Failed)
        }
    }

    @Test
    fun `saveNote - state should failed when updateNote is failed`() = runTest {
        // Arrange
        `when`(repository.updateNote(tId, tNoteDto)).thenAnswer {
            MutableStateFlow(Left(ServerFailure(tBaseErrorResponse)))
        }
        // Act
        val result = viewModel.saveNote(tNoteDto, tId)
        // Assert
        result.observeForTesting {
            verify(repository).updateNote(tId, tNoteDto)
            verify(repository, times(0)).createNote(tNoteDto)

            assertTrue(result.value is ResultState.Failed)
        }
    }
}