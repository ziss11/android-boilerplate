package com.ziss.notesappandroid.modules.note

import com.ziss.notesappandroid.modules.note.data.data_sources.local.NoteLocalDataSource
import com.ziss.notesappandroid.modules.note.data.data_sources.remote.NoteRemoteDataSource
import com.ziss.notesappandroid.modules.note.data.repositories.NoteRepository
import com.ziss.notesappandroid.modules.note.presentation.view_models.NoteDetailViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val noteModule = module {
    single { NoteRemoteDataSource(get()) }
    single { NoteLocalDataSource(get(), get()) }
    single { NoteRepository(get(), get()) }
    viewModel { NoteDetailViewModel(get()) }
}