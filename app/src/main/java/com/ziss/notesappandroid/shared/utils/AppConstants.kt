package com.ziss.notesappandroid.shared.utils

abstract class AppConstants private constructor() {
    object General : GeneralConstants()
    object DataStoreKey : DataStoreKeyConstants()
}

abstract class GeneralConstants {
    val dataStoreName = "settings"
}

abstract class DataStoreKeyConstants {
    val accessToken = "accessToken"
    val refreshToken = "refreshToken"
    val authSession = "authSession"
}