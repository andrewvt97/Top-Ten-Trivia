package com.example.toptentrivia.ui.navigation


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.toptentrivia.ui.AppViewModelProvider
import com.example.toptentrivia.ui.screens.UserViewModel
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
@RequiresApi(Build.VERSION_CODES.O)
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
    val userViewModel: UserViewModel = viewModel(factory = AppViewModelProvider.Factory)

    NavHost(
        navController = navController,
        startDestination = LoginDestination.route,
        modifier = modifier
    ) {

        composable(route = LoginDestination.route) {
            LoginScreen(
                userViewModel = userViewModel,
                navigateToSignUp = { navController.navigate(SignUpDestination.route) },
                navigateToHome = {
                    navController.navigate(HomeDestination.route)
                }
            )
        }

        composable(route = SignUpDestination.route) {
            SignUpScreen(
                userViewModel = userViewModel,
                navigateToLogin = { navController.navigate(LoginDestination.route) },
                navigateToHome = {
                    navController.navigate(HomeDestination.route)
                }
            )
        }


        composable(route = HomeDestination.route) {
            HomeScreen(
                userViewModel = userViewModel,
                onNavigateToQuiz = { navController.navigate(QuizDestination.route) },
                onNavigateToSummary = { navController.navigate(SummaryDestination.route) }
            )
        }


        composable(route = QuizDestination.route) {
            QuizScreen(
                navController = navController,
                viewModel = quizViewModel,
                userViewModel = userViewModel
            )
        }

        composable(route = SummaryDestination.route) {
            SummaryScreen(
                navController = navController,
                viewModel = quizViewModel,
                userViewModel = userViewModel
            )
        }

    }
}