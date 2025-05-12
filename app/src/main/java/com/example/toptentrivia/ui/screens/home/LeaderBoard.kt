// LeaderboardScreen.kt
package com.example.toptentrivia.ui.screens.leaderboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.toptentrivia.ui.navigation.NavigationDestination

object LeaderboardDestination : NavigationDestination {
    override val route = "leaderboard"
    override val titleRes = com.example.toptentrivia.R.string.leaderboard_title
}

data class User(
    val username: String,
    val pointsToday: Int,
    val averagePoints: Double,
    val gamesPlayedAllTime: Int,
    val correctAnswersAllTime: Int
)

val dummyUsers = listOf(
    User("Alice", 120, 18.5, 10, 85),
    User("Bob", 95, 15.2, 8, 60),
    User("Charlie", 135, 20.0, 12, 90),
    User("Diana", 110, 16.7, 11, 75),
    User("Eve", 105, 14.3, 9, 65)
)

@Composable
fun LeaderboardScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Leaderboard",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(dummyUsers) { user ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF00416A))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = user.username,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Points Today: ${user.pointsToday}",
                            color = Color.White
                        )
                        Text(
                            text = "Avg. Points: ${user.averagePoints}",
                            color = Color.White
                        )
                        Text(
                            text = "Games Played: ${user.gamesPlayedAllTime}",
                            color = Color.White
                        )
                        Text(
                            text = "Correct Answers: ${user.correctAnswersAllTime}",
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
