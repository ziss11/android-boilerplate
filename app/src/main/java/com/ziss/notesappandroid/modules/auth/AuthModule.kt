package com.ziss.notesappandroid.modules.auth

import com.ziss.notesappandroid.modules.auth.data.data_sources.local.AuthLocalDataSource
import com.ziss.notesappandroid.modules.auth.data.data_sources.remote.AuthRemoteDataSource
import com.ziss.notesappandroid.modules.auth.data.repositories.AuthRepository
import com.ziss.notesappandroid.modules.auth.presentation.view_models.AuthViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val authModule = module {
    single { AuthRemoteDataSource(get()) }
    single { AuthLocalDataSource(get()) }
    single { AuthRepository(get(), get(), get(), get()) }
    viewModel { AuthViewModel(get()) }
}