package com.ziss.notesappandroid.modules.note.data.models

import android.os.Parcelable
import com.ziss.notesappandroid.modules.note.data.data_sources.local.entities.NoteEntity
import com.ziss.notesappandroid.modules.user.data.data_sources.local.entities.UserEntity
import com.ziss.notesappandroid.modules.user.data.models.UserModel
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class NoteModel(
    val id: Int?,
    val title: String?,
    val content: String?,
    val createdAt: Date?,
    val updatedAt: Date?,
    val user: UserModel?,
) : Parcelable {
    constructor(note: NoteEntity, user: UserEntity?) : this(
        id = note.id,
        title = note.title,
        content = note.content,
        createdAt = Date(note.createdAt ?: 0),
        updatedAt = Date(note.updatedAt ?: 0),
        user = user?.let { UserModel(it) }
    )

    fun toEntity(): NoteEntity {
        return NoteEntity(
            id = id,
            title = title,
            content = content,
            userId = user?.id,
            createdAt = createdAt?.time ?: Date().time,
            updatedAt = updatedAt?.time ?: Date().time,
        )
    }
}
