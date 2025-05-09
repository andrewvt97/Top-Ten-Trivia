package com.example.toptentrivia.ui.screens.login

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel : ViewModel() {
    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState: StateFlow<LoginUiState> = _loginUiState

    fun onUsernameChange(newUsername: String) {
        _loginUiState.update { it.copy(username = newUsername) }
    }

    fun onPasswordChange(newPassword: String) {
        _loginUiState.update { it.copy(password = newPassword) }
    }

    fun login() {
        // validation
        val state = _loginUiState.value
        if (state.username.isBlank() || state.password.isBlank()) {
            _loginUiState.update { it.copy(errorMessage = "Please fill in all fields.") }
            return
        }

        // loading state
        _loginUiState.update { it.copy(isLoading = true, errorMessage = null) }

        // TODO: Call backend
    }
}

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)