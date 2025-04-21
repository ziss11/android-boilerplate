package com.ziss.notesappandroid.modules.home.presentation.view_models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ziss.notesappandroid.core.extensions.toArrayList
import com.ziss.notesappandroid.modules.note.data.models.NoteModel
import com.ziss.notesappandroid.modules.note.data.repositories.NoteRepository
import com.ziss.notesappandroid.shared.utils.AppUtils
import com.ziss.notesappandroid.shared.utils.ResultState
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: NoteRepository) : ViewModel() {
    var page by mutableStateOf<Int?>(1)
        private set
    var limit by mutableIntStateOf(10)
        private set
    var notesState = mutableStateOf<ResultState<List<NoteModel>>>(ResultState.Initial)
        private set

    fun fetchNotes(refresh: Boolean = false, loadMore: Boolean = false) {
        viewModelScope.launch {
            page = if (refresh) 1 else page
            if (page == null) return@launch

            if (page == 1) {
                notesState.value = ResultState.Loading
            }

            repository.fetchNotes(
                refresh = refresh,
                loadMore = loadMore,
                page = page,
                limit = limit
            ).collect { result ->
                result.fold({ failure ->
                    val message = AppUtils.getErrorMessage(
                        errors = failure.error?.errors?.toArrayList()
                    )
                    notesState.value = ResultState.Failed(message ?: "")
                }, { data ->
                    page = AppUtils.nextPage(page)

                    if (data.isEmpty()) {
                        if (refresh) {
                            notesState.value = ResultState.Initial
                            return@collect
                        }
                        notesState.value = ResultState.Success(data)
                    } else {
                        val currentState = notesState.value
                        notesState.value = ResultState.Success(
                            if (currentState is ResultState.Success)
                                currentState.data + data
                            else data
                        )
                    }
                })
            }
        }
    }
}