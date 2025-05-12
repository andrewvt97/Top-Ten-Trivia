package com.example.toptentrivia.ui.navigation


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.toptentrivia.ui.AppViewModelProvider
import com.example.toptentrivia.ui.screens.login.LoginDestination
import com.example.toptentrivia.ui.screens.login.LoginScreen
import com.example.toptentrivia.ui.screens.quiz.QuizDestination
import com.example.toptentrivia.ui.screens.quiz.QuizScreen
import com.example.toptentrivia.ui.screens.quiz.QuizViewModel
import com.example.toptentrivia.ui.screens.quiz.SummaryDestination
import com.example.toptentrivia.ui.screens.quiz.SummaryScreen
import com.example.toptentrivia.ui.screens.signup.SignUpDestination
import com.example.toptentrivia.ui.screens.signup.SignUpScreen

@Composable
fun TriviaNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {

    val quizViewModel: QuizViewModel = viewModel(
        factory = AppViewModelProvider.Factory
    )
    NavHost(
        navController = navController,
        startDestination = LoginDestination.route,
        modifier = modifier
    ) {
        composable(route = LoginDestination.route) {
            LoginScreen(
                navigateToSignUp = { navController.navigate(SignUpDestination.route) },
                navigateToQuiz = {
                    navController.navigate(QuizDestination.route) {
                        popUpTo(LoginDestination.route) { inclusive = true }
                    }
                }
            )
        }
        composable(route = SignUpDestination.route) {
            SignUpScreen(
                navigateToLogin = { navController.navigate(LoginDestination.route) },
                navigateToQuiz = {
                    navController.navigate(QuizDestination.route) {
                        popUpTo(SignUpDestination.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = QuizDestination.route) {
            QuizScreen(
                navController = navController,
                viewModel = quizViewModel
            )
        }
        composable(route = SummaryDestination.route) {
            SummaryScreen(
                navController = navController,
                viewModel = quizViewModel
            )
        }
    }
}