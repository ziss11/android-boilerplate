package com.ziss.notesappandroid.modules.user.data.data_sources.remote

import com.ziss.notesappandroid.modules.user.data.data_sources.remote.services.UserService

class UserRemoteDataSource(private val userService: UserService) {
    suspend fun getById(id: Int) = userService.getById(id)
}