package com.example.toptentrivia.ui



import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.toptentrivia.ui.screens.login.LoginViewModel
import com.example.toptentrivia.ui.screens.signup.SignUpViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for LoginViewModel
        initializer {
            LoginViewModel()
        }

        // Initializer for SignUpViewModel
        initializer {
            SignUpViewModel()
        }
    }
}