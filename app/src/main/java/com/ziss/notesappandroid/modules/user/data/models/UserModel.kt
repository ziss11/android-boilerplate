package com.ziss.notesappandroid.modules.user.data.models

import android.os.Parcelable
import com.ziss.notesappandroid.modules.user.data.data_sources.local.entities.UserEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    val id: Int?,
    val username: String?,
    val name: String?
) : Parcelable {
    constructor(user: UserEntity) : this(
        id = user.id,
        username = user.username,
        name = user.name
    )

    fun toEntity(): UserEntity {
        return UserEntity(
            id = id,
            username = username,
            name = name
        )
    }
}
