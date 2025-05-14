package com.example.toptentrivia.ui.navigation


import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.toptentrivia.ui.screens.leaderboard.LeaderboardDestination
import com.example.toptentrivia.ui.screens.leaderboard.LeaderboardScreen

import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.toptentrivia.ui.AppViewModelProvider
import com.example.toptentrivia.ui.screens.UserViewModel
import com.example.toptentrivia.ui.screens.home.HomeDestination
import com.example.toptentrivia.ui.screens.home.HomeScreen
import com.example.toptentrivia.ui.screens.login.LoginDestination
import com.example.toptentrivia.ui.screens.login.LoginScreen
import com.example.toptentrivia.ui.screens.logout.LogoutDestination
import com.example.toptentrivia.ui.screens.logout.LogoutScreen
import com.example.toptentrivia.ui.screens.navbar.BottomNavBar
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
    val quizViewModel: QuizViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val userViewModel: UserViewModel = viewModel(factory = AppViewModelProvider.Factory)

    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    // Routes where the BottomNavBar should be visible
    val showBottomBarRoutes = setOf("home", "leaderboard", "summary", "logout")



    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {

            // Main content
            Box(modifier = Modifier.weight(1f)) {
                NavHost(
                    navController = navController,
                    startDestination = LoginDestination.route,
                    modifier = modifier
                ) {
                    composable(route = LoginDestination.route) {
                        LoginScreen(
                            userViewModel = userViewModel,
                            navigateToSignUp = { navController.navigate(SignUpDestination.route) },
                            navigateToHome = { navController.navigate(HomeDestination.route) }
                        )
                    }

                    composable(route = SignUpDestination.route) {
                        SignUpScreen(
                            userViewModel = userViewModel,
                            navigateToLogin = { navController.navigate(LoginDestination.route) },
                            navigateToHome = { navController.navigate(HomeDestination.route) }
                        )
                    }

                    composable(route = HomeDestination.route) {
                        HomeScreen(
                            userViewModel = userViewModel,
                            onNavigateToQuiz = { navController.navigate(QuizDestination.route) },
                            onNavigateToSummary = {
                                navController.navigate(SummaryDestination.route) {
                                    popUpTo(HomeDestination.route) { inclusive = true }
                                }
                            }
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

                    composable(route = LeaderboardDestination.route) {
                        LeaderboardScreen(navController = navController)
                    }

                    composable(route = LogoutDestination.route) {
                        LogoutScreen(
                            userViewModel = userViewModel,
                            navigateToLogin = { navController.navigate(LoginDestination.route) },
                        )
                    }
                }
            }
            BackHandler(enabled = true) { /* no-op */ }

            // Show BottomNavBar only on specific routes
            if (currentRoute in showBottomBarRoutes) {
                BottomNavBar(
                    navController = navController,
                    currentRoute = currentRoute ?: ""
                )
            }
        }
    }
}
