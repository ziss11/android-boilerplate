package com.ziss.notesappandroid.modules.auth.presentation.view_models

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ziss.notesappandroid.modules.auth.data.models.AuthValidateModel
import com.ziss.notesappandroid.modules.auth.data.repositories.AuthRepository
import com.ziss.notesappandroid.shared.utils.ResultState
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {
    var authState = mutableStateOf<ResultState<AuthValidateModel>>(ResultState.Initial)
        private set

    fun authCheck() {
        viewModelScope.launch {
            repository.validateAuth().collect { result ->
                result.fold({ failure ->
                    authState.value = ResultState.Failed()
                }, { data ->
                    authState.value = ResultState.Success(data)
                })
            }
        }
    }
}