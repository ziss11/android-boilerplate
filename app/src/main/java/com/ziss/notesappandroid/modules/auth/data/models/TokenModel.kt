package com.ziss.notesappandroid.modules.auth.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TokenModel(
    val accessToken: String?,
    val refreshToken: String?
) : Parcelable
