package com.ziss.notesappandroid.modules.auth.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AuthValidateModel(
    val id: Int?,
    val username: String?,
    val hasGroups: List<String>?,
    val hasPermissions: List<String>?,
) : Parcelable
