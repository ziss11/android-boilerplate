package com.ziss.notesappandroid.modules.user.data.data_sources.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey()
    @ColumnInfo(name = "id")
    val id: Int?,

    @ColumnInfo(name = "username")
    val username: String?,

    @ColumnInfo(name = "name")
    val name: String?
)
