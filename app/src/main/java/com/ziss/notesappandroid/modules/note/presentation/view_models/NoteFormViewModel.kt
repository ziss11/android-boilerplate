package com.ziss.notesappandroid.modules.note.presentation.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.ziss.notesappandroid.core.extensions.toArrayList
import com.ziss.notesappandroid.modules.note.data.models.NoteDto
import com.ziss.notesappandroid.modules.note.data.repositories.NoteRepository
import com.ziss.notesappandroid.shared.utils.AppUtils
import com.ziss.notesappandroid.shared.utils.ResultState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn

class NoteFormViewModel(private val repository: NoteRepository) : ViewModel() {
    fun saveNote(note: NoteDto, id: Int? = null) = liveData {
        emit(ResultState.Loading)

        val result = if (id == null) {
            repository.createNote(note)
        } else {
            repository.updateNote(id, note)
        }
        result.flowOn(Dispatchers.IO).collect {
            it.fold({ failure ->
                val message = AppUtils.getErrorMessage(failure.error?.errors?.toArrayList())
                emit(ResultState.Failed(message ?: ""))
            }, { data ->
                emit(ResultState.Success(data))
            })
        }
    }
}