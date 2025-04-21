package com.ziss.notesappandroid.modules.note.data.data_sources.local

import com.ziss.notesappandroid.core.common.DatabaseException
import com.ziss.notesappandroid.modules.note.data.data_sources.local.db.NoteDao
import com.ziss.notesappandroid.modules.note.data.models.NoteModel
import com.ziss.notesappandroid.modules.user.data.data_sources.local.db.UserDao

class NoteLocalDataSource(private val noteDao: NoteDao, private val userDao: UserDao) {
    suspend fun findAll(): List<NoteModel> {
        val noteEntities = (noteDao.findAll()).reversed()
        val noteModels = mutableListOf<NoteModel>()

        for (noteEntity in noteEntities) {
            val userEntity = userDao.findById(noteEntity.userId ?: 0)
            noteModels.add(NoteModel(noteEntity, userEntity))
        }

        return noteModels.toList()
    }

    suspend fun insertAll(notes: List<NoteModel>): String {
        try {
            val noteEntities = notes.map { it.toEntity() }
            val userEntities = notes.mapNotNull { it.user?.toEntity() }

            noteDao.insertAll(noteEntities)
            userDao.insertAll(userEntities)

            return "Notes Inserted"
        } catch (e: Exception) {
            throw DatabaseException(e.message)
        }
    }

    suspend fun clearAll(): String {
        try {
            noteDao.clearAll()
            return "Notes Cleared"
        } catch (e: Exception) {
            throw DatabaseException(e.message)
        }
    }
}