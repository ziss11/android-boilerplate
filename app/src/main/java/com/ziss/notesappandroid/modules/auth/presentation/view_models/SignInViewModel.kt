package com.ziss.notesappandroid.modules.auth.presentation.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ziss.notesappandroid.core.extensions.toArrayList
import com.ziss.notesappandroid.modules.auth.data.models.SignInDto
import com.ziss.notesappandroid.modules.auth.data.repositories.AuthRepository
import com.ziss.notesappandroid.shared.utils.AppUtils
import com.ziss.notesappandroid.shared.utils.ResultState
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class SignInViewModel(private val repository: AuthRepository) : ViewModel() {
    fun signIn(payload: SignInDto) = repository.signIn(payload)
        .map { result ->
            result.fold({ failure ->
                val message = AppUtils.getErrorMessage(
                    errors = failure.error?.errors?.toArrayList()
                )
                ResultState.Failed(message ?: "")
            }, {
                ResultState.Success(it)
            })
        }
        .onStart { emit(ResultState.Loading) }
        .asLiveData()
}