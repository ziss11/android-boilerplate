package com.ziss.notesappandroid.modules.note.data.data_sources.remote

import com.ziss.notesappandroid.dummies.DummyObject.getResponse
import com.ziss.notesappandroid.dummies.DummyObject.tNoteDto
import com.ziss.notesappandroid.dummies.DummyObject.tNoteModel
import com.ziss.notesappandroid.modules.note.data.data_sources.remote.services.NoteService
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class NoteRemoteDataSourceTest {
    private lateinit var service: NoteService
    private lateinit var remoteDataSource: NoteRemoteDataSource

    @Before
    fun setUp() {
        service = mock(NoteService::class.java)
        remoteDataSource = NoteRemoteDataSource(service)
    }

    private val tId = 1

    private val tPage = 1
    private val tLimit = 10

    @Test
    fun `create - should return created note data when success`() = runTest {
        // Arrange
        `when`(service.create(tNoteDto)).thenAnswer { getResponse(tNoteModel) }
        // Act
        val result = remoteDataSource.create(tNoteDto)
        // Assert
        assertEquals(result, getResponse(tNoteModel))
    }

    @Test
    fun `delete - should return success message when success`() = runTest {
        // Arrange
        `when`(service.delete(tId)).thenAnswer { "Note Deleted" }
        // Act
        val result = remoteDataSource.delete(tId)
        // Assert
        assertEquals(result, "Note Deleted")
    }

    @Test
    fun `fetch - should return list of note when success`() = runTest {
        // Arrange
        `when`(service.fetch(tPage, tLimit)).thenAnswer { getResponse(listOf(tNoteModel)) }
        // Act
        val result = remoteDataSource.fetch(tPage, tLimit)
        // Assert
        assertEquals(result, getResponse(listOf(tNoteModel)))
    }

    @Test
    fun `update - should return updated note when success`() = runTest {
        // Arrange
        `when`(service.update(tId, tNoteDto)).thenAnswer { getResponse(tNoteModel) }
        // Act
        val result = remoteDataSource.update(tId, tNoteDto)
        // Assert
        assertEquals(result, getResponse(tNoteModel))
    }

    @Test
    fun `getById - should return note when success`() = runTest {
        // Arrange
        `when`(service.getById(tId)).thenAnswer { getResponse(tNoteModel) }
        // Act
        val result = remoteDataSource.getById(tId)
        // Assert
        assertEquals(result, getResponse(tNoteModel))
    }
}