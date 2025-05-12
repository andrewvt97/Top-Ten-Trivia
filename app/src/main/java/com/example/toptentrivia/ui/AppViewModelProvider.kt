package com.example.toptentrivia.ui



import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.toptentrivia.TopTenTriviaApplication
import com.example.toptentrivia.ui.screens.login.LoginViewModel
import com.example.toptentrivia.ui.screens.signup.SignUpViewModel
import com.example.toptentrivia.ui.screens.quiz.QuizViewModel
import com.example.toptentrivia.ui.screens.home.HomeViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for LoginViewModel
        initializer {
            val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as TopTenTriviaApplication
            LoginViewModel(app.container.userRepository)
        }

        // Initializer for SignUpViewModel
        initializer {
            val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as TopTenTriviaApplication
            SignUpViewModel(app.container.userRepository)
        }

        // Initializer for HomeViewModel
        initializer {
            val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as TopTenTriviaApplication
            HomeViewModel(app.container.userRepository)
        }

        // Initializer for QuizViewModel
        initializer {
            val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as TopTenTriviaApplication)
            QuizViewModel(
                app.container.triviaRepository,
                userRepository = app.container.userRepository
            )
        }
    }
}