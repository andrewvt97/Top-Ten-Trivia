package com.example.toptentrivia.ui.screens.signup

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class SignUpViewModel : ViewModel() {
    private val _signUpUiState = MutableStateFlow(SignUpUiState())
    val signUpUiState: StateFlow<SignUpUiState> = _signUpUiState

    fun onEmailChange(newEmail: String) {
        _signUpUiState.update { it.copy(email = newEmail) }
    }

    fun onUsernameChange(newUsername: String) {
        _signUpUiState.update { it.copy(username = newUsername) }
    }

    fun onPasswordChange(newPassword: String) {
        _signUpUiState.update { it.copy(password = newPassword) }
    }

    fun signUp() {
        val state = _signUpUiState.value
        if (state.email.isBlank() || state.username.isBlank() || state.password.isBlank()) {
            _signUpUiState.update { it.copy(errorMessage = "Please fill in all fields.") }
            return
        }

        _signUpUiState.update { it.copy(isLoading = true, errorMessage = null) }

        // TODO: Call backend
    }
}

data class SignUpUiState(
    val email: String = "",
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
