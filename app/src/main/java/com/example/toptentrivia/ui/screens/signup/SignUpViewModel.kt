package com.example.toptentrivia.ui.screens.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.toptentrivia.data.UserRepository
import com.example.toptentrivia.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale

class SignUpViewModel (
    private val userRepository: UserRepository
): ViewModel() {
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


        viewModelScope.launch {

            val newUser = User(
                email = state.email,
                username = state.username,
                password = state.password,
            )
            try {
                val result = userRepository.registerUser(newUser)
                if (result.isSuccess) {
                    _signUpUiState.update { it.copy(isLoading = false, errorMessage = null, signUpSuccessful = true) }
                } else {
                    _signUpUiState.update { it.copy(isLoading = false, errorMessage = result.exceptionOrNull()?.message) }
                }
            } catch (e: Exception) {
                _signUpUiState.update {
                    it.copy(isLoading = false, errorMessage = "Sign up failed: ${e.message}")
                }
            }
        }
    }
}

data class SignUpUiState(
    val email: String = "",
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val signUpSuccessful: Boolean = false
)
