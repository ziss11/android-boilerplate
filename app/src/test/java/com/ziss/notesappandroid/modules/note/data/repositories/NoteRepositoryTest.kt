package com.ziss.notesappandroid.modules.note.data.repositories

import arrow.core.Either.Left
import arrow.core.Either.Right
import com.ziss.notesappandroid.core.common.ApiException
import com.ziss.notesappandroid.core.common.ServerFailure
import com.ziss.notesappandroid.dummies.DummyObject.getResponse
import com.ziss.notesappandroid.dummies.DummyObject.tBaseErrorResponse
import com.ziss.notesappandroid.dummies.DummyObject.tNoteDto
import com.ziss.notesappandroid.dummies.DummyObject.tNoteModel
import com.ziss.notesappandroid.modules.note.data.data_sources.local.NoteLocalDataSource
import com.ziss.notesappandroid.modules.note.data.data_sources.remote.NoteRemoteDataSource
import com.ziss.notesappandroid.modules.note.data.models.NoteModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class NoteRepositoryTest {
    private lateinit var remoteDataSource: NoteRemoteDataSource
    private lateinit var localDataSource: NoteLocalDataSource
    private lateinit var repository: NoteRepository

    @Before
    fun setUp() {
        remoteDataSource = mock(NoteRemoteDataSource::class.java)
        localDataSource = mock(NoteLocalDataSource::class.java)
        repository = NoteRepository(remoteDataSource, localDataSource)
    }

    private val tId = 1

    private val tPage = 1
    private val tLimit = 10

    @Test
    fun `createNote - should return note when success`() = runTest {
        // Arrange
        `when`(remoteDataSource.create(tNoteDto)).thenAnswer { getResponse(tNoteModel) }
        `when`(localDataSource.clearAll()).thenAnswer { "Notes cleared" }
        // Act
        val result = repository.createNote(tNoteDto).first()
        // Assert
        verify(localDataSource).clearAll()
        assertEquals(result, Right(getResponse(tNoteModel)))
    }

    @Test
    fun `createNote - should ServerFailure when failed`() = runTest {
        // Arrange
        `when`(remoteDataSource.create(tNoteDto)).thenThrow(
            ApiException(error = tBaseErrorResponse)
        )
        // Act
        val result = repository.createNote(tNoteDto)
        // Assert
        assertEquals(result.first(), Left(ServerFailure(tBaseErrorResponse)))
    }

    @Test
    fun `deleteNote - should return success message when success`() = runTest {
        // Arrange
        `when`(remoteDataSource.delete(tId)).thenAnswer { "Note Deleted" }
        // Act
        val result = repository.deleteNote(tId)
        // Assert
        assertEquals(result.first(), Right("Note Deleted"))
    }

    @Test
    fun `deleteNote - should return ServerFailure when success`() = runTest {
        // Arrange
        `when`(remoteDataSource.delete(tId)).thenThrow(
            ApiException(tBaseErrorResponse)
        )
        // Act
        val result = repository.deleteNote(tId)
        // Assert
        assertEquals(result.first(), Left(ServerFailure(tBaseErrorResponse)))
    }

    @Test
    fun `fetchNotes - should return notes from local when data exists and not refreshing or load more`() =
        runTest {
            // Arrange
            `when`(localDataSource.findAll()).thenAnswer { listOf(tNoteModel) }
            // Act
            val result = repository.fetchNotes().first()
            // Assert
            verify(localDataSource).findAll()
            verify(remoteDataSource, times(0)).fetch(tPage, tLimit)
            assertEquals(result, Right(listOf(tNoteModel)))
        }

    @Test
    fun `fetchNotes - should return notes from remote when refreshing`() = runTest {
        // Arrange
        `when`(localDataSource.findAll()).thenAnswer { listOf<NoteModel>() }
        `when`(remoteDataSource.fetch(tPage, tLimit)).thenAnswer { getResponse(listOf(tNoteModel)) }
        `when`(localDataSource.clearAll()).thenAnswer { "Notes cleared" }
        `when`(localDataSource.insertAll(listOf(tNoteModel))).thenAnswer { "Notes inserted" }
        // Act
        val result = repository.fetchNotes(refresh = true).first()
        // Assert
        verify(localDataSource).findAll()
        verify(remoteDataSource).fetch(tPage, tLimit)
        verify(localDataSource).clearAll()
        verify(localDataSource).insertAll(listOf(tNoteModel))
        assertEquals(result, Right(listOf(tNoteModel)))
    }

    @Test
    fun `fetchNotes - should return notes from remote when loading more`() = runTest {
        // Arrange
        `when`(localDataSource.findAll()).thenAnswer { listOf<NoteModel>() }
        `when`(remoteDataSource.fetch(tPage, tLimit)).thenAnswer {
            getResponse(listOf(tNoteModel))
        }
        // Act
        val result = repository.fetchNotes(loadMore = true).first()
        // Assert
        verify(localDataSource).findAll()
        verify(remoteDataSource).fetch(tPage, tLimit)
        assertEquals(result, Right(listOf(tNoteModel)))
    }

    @Test
    fun `fetchNotes - should return notes from remote when cache is empty`() = runTest {
        // Arrange
        `when`(localDataSource.findAll()).thenAnswer { listOf<NoteModel>() }
        `when`(remoteDataSource.fetch(tPage, tLimit)).thenAnswer {
            getResponse(listOf(tNoteModel))
        }
        // Act
        val result = repository.fetchNotes().first()
        // Assert
        verify(localDataSource).findAll()
        verify(remoteDataSource).fetch(tPage, tLimit)
        assertEquals(result, Right(listOf(tNoteModel)))
    }

    @Test
    fun `fetchNotes - should return notes from remote with custom limit`() = runTest {
        // Arrange
        `when`(localDataSource.findAll()).thenAnswer { listOf<NoteModel>() }
        `when`(remoteDataSource.fetch(tPage, 20)).thenAnswer {
            getResponse(listOf(tNoteModel))
        }
        // Act
        val result = repository.fetchNotes(limit = 20).first()
        // Assert
        verify(localDataSource).findAll()
        verify(remoteDataSource).fetch(tPage, 20)
        assertEquals(result, Right(listOf(tNoteModel)))
    }

    @Test
    fun `fetchNotes - should return ServerFailure when failed`() = runTest {
        // Arrange
        `when`(localDataSource.findAll()).thenAnswer { listOf<NoteModel>() }
        `when`(remoteDataSource.fetch(tPage, tLimit)).thenThrow(
            ApiException(tBaseErrorResponse)
        )
        // Act
        val result = repository.fetchNotes().first()
        // Assert
        verify(localDataSource).findAll()
        verify(remoteDataSource).fetch(tPage, tLimit)
        assertEquals(result, Left(ServerFailure(tBaseErrorResponse)))
    }

    @Test
    fun `getNoteById - should return note with given id when success`() = runTest {
        // Arrange
        `when`(remoteDataSource.getById(tId)).thenAnswer { getResponse(tNoteModel) }
        // Act
        val result = repository.getNoteById(tId).first()
        // Assert
        assertEquals(result, Right(getResponse(tNoteModel)))
    }

    @Test
    fun `getNoteById - should return ServerFailure when failed`() = runTest {
        // Arrange
        `when`(remoteDataSource.getById(tId)).thenThrow(
            ApiException(tBaseErrorResponse)
        )
        // Act
        val result = repository.getNoteById(tId).first()
        // Assert
        assertEquals(result, Left(ServerFailure(tBaseErrorResponse)))
    }

    @Test
    fun `updateNote - should return updated note when success`() = runTest {
        // Arrange
        `when`(remoteDataSource.update(tId, tNoteDto)).thenAnswer { getResponse(tNoteModel) }
        `when`(localDataSource.clearAll()).thenAnswer { "Notes cleared" }
        // Act
        val result = repository.updateNote(tId, tNoteDto).first()
        // Assert
        verify(localDataSource).clearAll()
        assertEquals(result, Right(getResponse(tNoteModel)))
    }

    @Test
    fun `updateNote - should return ServerFailed when failed`() = runTest {
        // Arrange
        `when`(remoteDataSource.update(tId, tNoteDto)).thenThrow(
            ApiException(tBaseErrorResponse)
        )
        // Act
        val result = repository.updateNote(tId, tNoteDto)
        // Assert
        assertEquals(result.first(), Left(ServerFailure(tBaseErrorResponse)))
    }
}