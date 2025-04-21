package com.ziss.notesappandroid.modules.user.data.data_sources.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ziss.notesappandroid.modules.user.data.data_sources.local.entities.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    suspend fun findAll(): List<UserEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<UserEntity>)

    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun findById(id: Int): UserEntity?

    @Query("DELETE FROM users")
    suspend fun clearAll()
}