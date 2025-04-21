package com.ziss.notesappandroid.modules.note.data.data_sources.local

import com.ziss.notesappandroid.core.common.DatabaseException
import com.ziss.notesappandroid.dummies.DummyObject.tNoteEntity
import com.ziss.notesappandroid.dummies.DummyObject.tNoteModel
import com.ziss.notesappandroid.dummies.DummyObject.tUserEntity
import com.ziss.notesappandroid.modules.note.data.data_sources.local.db.NoteDao
import com.ziss.notesappandroid.modules.user.data.data_sources.local.db.UserDao
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class NoteLocalDataSourceTest {
    private lateinit var noteDao: NoteDao
    private lateinit var userDao: UserDao
    private lateinit var localDataSource: NoteLocalDataSource

    @Before
    fun setUp() {
        noteDao = mock(NoteDao::class.java)
        userDao = mock(UserDao::class.java)
        localDataSource = NoteLocalDataSource(noteDao, userDao)
    }

    @Test
    fun `findAll - should return notes when success`() = runTest {
        // Arrange
        `when`(noteDao.findAll()).thenAnswer { listOf(tNoteEntity) }
        `when`(userDao.findById(tUserEntity.id ?: 0)).thenAnswer { tUserEntity }
        // Act
        val result = localDataSource.findAll()
        // Assert
        assertEquals(result, listOf(tNoteModel))
    }

    @Test
    fun `insertAll - should return success message when success`() = runTest {
        // Arrange
        `when`(noteDao.insertAll(listOf(tNoteEntity))).thenAnswer {}
        `when`(userDao.insertAll(listOf(tUserEntity))).thenAnswer {}
        // Act
        val result = localDataSource.insertAll(listOf(tNoteModel))
        // Assert
        assertEquals(result, "Notes Inserted")
    }

    @Test
    fun `insertAll - should throw DatabaseException when user not inserted`() = runTest {
        // Arrange
        `when`(userDao.insertAll(listOf(tUserEntity))).thenThrow(RuntimeException())
        // Act && Assert
        assertThrows(DatabaseException::class.java) {
            runBlocking {
                localDataSource.insertAll(listOf(tNoteModel))
            }
        }
    }

    @Test
    fun `clearAll - should return success message when success`() = runTest {
        // Arrange
        `when`(noteDao.clearAll()).thenAnswer {}
        // Act
        val result = localDataSource.clearAll()
        // Assert
        assertEquals(result, "Notes Cleared")
    }

    @Test
    fun `clearAll - should throw DatabaseException when failed`() = runTest {
        // Arrange
        `when`(noteDao.clearAll()).thenThrow(RuntimeException())
        // Act && Assert
        assertThrows(DatabaseException::class.java) {
            runBlocking {
                localDataSource.clearAll()
            }
        }
    }
}