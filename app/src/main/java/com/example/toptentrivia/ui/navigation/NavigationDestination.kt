package com.example.toptentrivia.ui.navigation

interface NavigationDestination {
    val route: String
    val titleRes: Int
    fun createRoute(username: String) = "home/$username"
}