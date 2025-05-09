package com.example.toptentrivia.ui.navigation


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.toptentrivia.ui.screens.login.LoginDestination
import com.example.toptentrivia.ui.screens.login.LoginScreen
import com.example.toptentrivia.ui.screens.signup.SignUpDestination
import com.example.toptentrivia.ui.screens.signup.SignUpScreen

@Composable
fun TriviaNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = LoginDestination.route,
        modifier = modifier
    ) {
        composable(route = LoginDestination.route) {
            LoginScreen(
                navigateToSignUp = { navController.navigate(SignUpDestination.route) }
            )
        }
        composable(route = SignUpDestination.route) {
            SignUpScreen(
                navigateToLogin = { navController.navigate(LoginDestination.route) }
            )
        }
    }
}