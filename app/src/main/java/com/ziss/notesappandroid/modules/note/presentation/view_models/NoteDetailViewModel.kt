package com.ziss.notesappandroid.modules.note.presentation.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ziss.notesappandroid.core.extensions.toArrayList
import com.ziss.notesappandroid.modules.note.data.repositories.NoteRepository
import com.ziss.notesappandroid.shared.utils.AppUtils
import com.ziss.notesappandroid.shared.utils.ResultState
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class NoteDetailViewModel(private val repository: NoteRepository) : ViewModel() {
    fun getNoteById(id: Int) = repository.getNoteById(id)
        .map { result ->
            result.fold({ failure ->
                val message = AppUtils.getErrorMessage(
                    errors = failure.error?.errors?.toArrayList()
                )
                ResultState.Failed(message ?: "")
            }, { data ->
                ResultState.Success(data.data)
            })
        }
        .onStart { emit(ResultState.Loading) }
        .asLiveData()

    fun deleteNote(id: Int) = repository.deleteNote(id)
        .map { result ->
            result.fold({ failure ->
                val message = AppUtils.getErrorMessage(
                    errors = failure.error?.errors?.toArrayList()
                )
                ResultState.Failed(message ?: "")
            }, { data ->
                ResultState.Success(data)
            })
        }
        .onStart { emit(ResultState.Loading) }
        .asLiveData()
}