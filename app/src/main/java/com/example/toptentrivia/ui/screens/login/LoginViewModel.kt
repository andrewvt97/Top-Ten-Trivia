package com.example.toptentrivia.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.toptentrivia.data.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class LoginViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState: StateFlow<LoginUiState> = _loginUiState

    fun onUsernameChange(newUsername: String) {
        _loginUiState.update { it.copy(username = newUsername) }
    }

    fun onPasswordChange(newPassword: String) {
        _loginUiState.update { it.copy(password = newPassword) }
    }

    fun login() {
        val state = _loginUiState.value
        if (state.username.isBlank() || state.password.isBlank()) {
            _loginUiState.update { it.copy(errorMessage = "Please fill in all fields.") }
            return
        }

        _loginUiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            try {
                val result = userRepository.loginUser(state.username, state.password)

                if (result.isSuccess) {
                    _loginUiState.update { it.copy(isLoading = false, loginSuccessful = true) }
                } else {
                    _loginUiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.exceptionOrNull()?.message ?: "Invalid username or password."
                        )
                    }
                }
            } catch (e: Exception) {
                _loginUiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Login failed: ${e.message}"
                    )
                }
            }
        }
    }
}

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val loginSuccessful: Boolean = false
)