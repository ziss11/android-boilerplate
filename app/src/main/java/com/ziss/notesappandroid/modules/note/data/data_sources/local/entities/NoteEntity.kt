package com.ziss.notesappandroid.modules.note.data.data_sources.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.ziss.notesappandroid.modules.user.data.data_sources.local.entities.UserEntity

@Entity(
    tableName = "notes",
    foreignKeys = [
        ForeignKey(
            childColumns = ["userId"],
            parentColumns = ["id"],
            entity = UserEntity::class,
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class NoteEntity(
    @PrimaryKey()
    @ColumnInfo(name = "id")
    val id: Int?,

    @ColumnInfo(name = "title")
    val title: String?,

    @ColumnInfo(name = "content")
    val content: String?,

    @ColumnInfo(name = "userId")
    val userId: Int?,

    @ColumnInfo(name = "createdAt")
    val createdAt: Long?,

    @ColumnInfo(name = "updatedAt")
    val updatedAt: Long?,
)
