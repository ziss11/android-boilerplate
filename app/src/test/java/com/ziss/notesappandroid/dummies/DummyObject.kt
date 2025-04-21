package com.ziss.notesappandroid.dummies

import com.ziss.notesappandroid.modules.auth.data.models.AuthValidateModel
import com.ziss.notesappandroid.modules.auth.data.models.LogoutDto
import com.ziss.notesappandroid.modules.auth.data.models.SignInDto
import com.ziss.notesappandroid.modules.auth.data.models.TokenModel
import com.ziss.notesappandroid.modules.note.data.data_sources.local.entities.NoteEntity
import com.ziss.notesappandroid.modules.note.data.models.NoteDto
import com.ziss.notesappandroid.modules.note.data.models.NoteModel
import com.ziss.notesappandroid.modules.user.data.data_sources.local.entities.UserEntity
import com.ziss.notesappandroid.modules.user.data.models.UserModel
import com.ziss.notesappandroid.shared.responses.BaseErrorResponse
import com.ziss.notesappandroid.shared.responses.BaseResponse
import com.ziss.notesappandroid.shared.responses.ErrorDetailResponse
import com.ziss.notesappandroid.shared.responses.MetaResponse
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date


val defaultDate: Date = Date.from(
    LocalDate.of(2025, 1, 1)
        .atStartOfDay(ZoneId.systemDefault())
        .toInstant()
)

object DummyObject {
    fun <T> getResponse(data: T) = BaseResponse(
        data = data,
        meta = tMetaResponse
    )

    val tMetaResponse = MetaResponse(
        page = 1,
        totalData = 10,
        totalPage = 1
    )
    val tBaseErrorResponse = BaseErrorResponse(
        type = "type",
        errors = listOf(
            ErrorDetailResponse(
                detail = "detail",
                attr = "attr"
            )
        )
    )
    val tUserModel = UserModel(
        id = 1,
        username = "username",
        name = "name"
    )
    val tUserEntity = UserEntity(
        id = 1,
        username = "username",
        name = "name"
    )
    val tNoteDto = NoteDto(
        title = "title",
        content = "content"
    )
    val tNoteModel = NoteModel(
        id = 1,
        title = "Title",
        content = "Content",
        createdAt = defaultDate,
        updatedAt = defaultDate,
        user = tUserModel
    )
    val tNoteEntity = NoteEntity(
        id = 1,
        title = "Title",
        content = "Content",
        userId = tUserModel.id,
        createdAt = defaultDate.time,
        updatedAt = defaultDate.time,
    )
    val tAuthValidateModel = AuthValidateModel(
        id = 1,
        username = "username",
        hasGroups = listOf("group1", "group2"),
        hasPermissions = listOf("permission1", "permission2")
    )
    val tTokenModel = TokenModel(
        accessToken = "accessToken",
        refreshToken = "refreshToken"
    )
    val tSignInDto = SignInDto(
        username = "username",
        password = "password"
    )
    val tLogoutDto = LogoutDto(
        refreshToken = "refreshToken"
    )
}