package com.ziss.notesappandroid.core.services

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ziss.notesappandroid.modules.note.data.data_sources.local.db.NoteDao
import com.ziss.notesappandroid.modules.note.data.data_sources.local.entities.NoteEntity
import com.ziss.notesappandroid.modules.user.data.data_sources.local.db.UserDao
import com.ziss.notesappandroid.modules.user.data.data_sources.local.entities.UserEntity

@Database(
    entities = [
        UserEntity::class,
        NoteEntity::class
    ],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun noteDao(): NoteDao
}