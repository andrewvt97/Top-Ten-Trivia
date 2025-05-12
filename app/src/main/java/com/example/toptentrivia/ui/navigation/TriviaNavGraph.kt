package com.example.toptentrivia.ui.navigation


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.toptentrivia.ui.AppViewModelProvider
import com.example.toptentrivia.ui.screens.home.HomeDestination
import com.example.toptentrivia.ui.screens.home.HomeScreen
import com.example.toptentrivia.ui.screens.login.LoginDestination
import com.example.toptentrivia.ui.screens.login.LoginScreen
import com.example.toptentrivia.ui.screens.quiz.QuizDestination
import com.example.toptentrivia.ui.screens.quiz.QuizScreen
import com.example.toptentrivia.ui.screens.quiz.QuizViewModel
import com.example.toptentrivia.ui.screens.quiz.SummaryDestination
import com.example.toptentrivia.ui.screens.quiz.SummaryScreen
import com.example.toptentrivia.ui.screens.signup.SignUpDestination
import com.example.toptentrivia.ui.screens.signup.SignUpScreen

//handles navigation of entire app
@Composable
fun TriviaNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    // viewModel object instance creation
    val quizViewModel: QuizViewModel = viewModel(
        // .Factory creates and retrieves instance via Compose MAGIC
        factory = AppViewModelProvider.Factory
    )

    NavHost(
        navController = navController,
        startDestination = LoginDestination.route,
        modifier = modifier
    ) {
//        composable(route = LoginDestination.route) {
//            LoginScreen(
//                navigateToSignUp = { navController.navigate(SignUpDestination.route) },
//                navigateToHome = {
//                    navController.navigate(HomeDestination.route) {
//                        popUpTo(LoginDestination.route) { inclusive = true }
//                    }
//                }
//            )
//        }
        composable(route = LoginDestination.route) {
            LoginScreen(
                navigateToSignUp = { navController.navigate(SignUpDestination.route) },
                navigateToHome = { username ->
                    navController.navigate(HomeDestination.createRoute(username)) {
                        popUpTo(LoginDestination.route) { inclusive = true }
                    }
                }
            )
        }
        composable(route = SignUpDestination.route) {
            SignUpScreen(
                navigateToLogin = { navController.navigate(LoginDestination.route) },
                navigateToHome = { username ->
                    navController.navigate(HomeDestination.createRoute(username)) {
                        popUpTo(SignUpDestination.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = "home/{username}") { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: return@composable
            HomeScreen(
                username = username,
                onNavigateToQuiz = { username ->
                    navController.navigate(QuizDestination.createRoute(username))
                },
                onNavigateToSummary = { navController.navigate(SummaryDestination.route) }
            )
        }
//        composable(route = QuizDestination.route) {
//            QuizScreen(
//                navController = navController,
//                viewModel = quizViewModel
//            )
//        }
        composable(route = "quiz/{username}") { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: return@composable
            QuizScreen(
                navController = navController,
                viewModel = quizViewModel,
                username = username
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