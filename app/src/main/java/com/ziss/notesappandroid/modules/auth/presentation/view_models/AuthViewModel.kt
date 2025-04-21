package com.ziss.notesappandroid.modules.auth.presentation.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ziss.notesappandroid.modules.auth.data.repositories.AuthRepository
import com.ziss.notesappandroid.shared.utils.ResultState
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {
    fun authCheck() = repository.validateAuth()
        .map { result ->
            result.fold({ failure ->
                ResultState.Failed()
            }, { data ->
                ResultState.Success(data)
            })
        }
        .onStart { emit(ResultState.Loading) }
        .asLiveData()
}