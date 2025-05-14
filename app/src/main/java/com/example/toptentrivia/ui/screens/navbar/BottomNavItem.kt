package com.example.toptentrivia.ui.screens.navbar

import com.example.toptentrivia.R

sealed class BottomNavItem(val route: String, val icon: Int, val label: String) {
    object Home : BottomNavItem("home", R.drawable.home, "Home")
    object Leaderboard : BottomNavItem("leaderboard", R.drawable.leaderboard, "Leaderboard")
    object Profile : BottomNavItem("summary", R.drawable.summary, "Profile")
    object Settings : BottomNavItem("logout", R.drawable.settings, "Settings")

    companion object {
        val allItems = listOf(Home, Leaderboard, Profile, Settings)
    }
}