
package com.example.toptentrivia.ui.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
/*import androidx.compose.foundation.layout.ColumnScopeInstance.weight
import androidx.compose.foundation.layout.FlowColumnScopeInstance.weight*/
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.toptentrivia.R
import com.example.toptentrivia.data.model.User
import com.example.toptentrivia.ui.AppViewModelProvider
import com.example.toptentrivia.ui.navigation.NavigationDestination
import com.example.toptentrivia.ui.screens.UserViewModel
import com.example.toptentrivia.ui.theme.TopTenTriviaTheme


object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.home_title
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen (
    onNavigateToQuiz: () -> Unit,
    onNavigateToSummary: () -> Unit,
    userViewModel: UserViewModel,
) {

    val user = userViewModel.user.collectAsState()



    val streakRank = remember { mutableStateOf<Int?>(null) }
    val totalPointsRank = remember { mutableStateOf<Int?>(null) }
    val todayPointsRank = remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(Unit) {
        userViewModel.getUserStreakRanking { streakRank.value = it }
        userViewModel.getUserTotalPointsRanking { totalPointsRank.value = it }
        userViewModel.getUserTodayRanking { todayPointsRank.value = it }
    }

    user.value?.let { u ->
        Column(modifier = Modifier.fillMaxSize()) {
            HomeTopBar(u.username)
            HomeContent(
                user = u,
                onNavigateToQuiz = onNavigateToQuiz,
                onNavigateToSummary = onNavigateToSummary,
                streakRank = streakRank,
                totalPointsRank = totalPointsRank,
                todayPointsRank = todayPointsRank
            )
            BottomNavBar()
        }
    }

}

@Composable
private fun HomeTopBar(username: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF0A2742))
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Top Ten Trivia",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Welcome back, $username!",
                color = Color.White,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Profile avatar
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.3f))
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.default_profile_icon),
                    contentDescription = "Profile",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}



@Composable
private fun HomeContent(
    user: User,
    streakRank: State<Int?>,
    totalPointsRank: State<Int?>,
    todayPointsRank: State<Int?>,
    onNavigateToQuiz: () -> Unit,
    onNavigateToSummary: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            StreakCard(user,)
            Spacer(modifier = Modifier.height(16.dp))
            StatsCard(user,streakRank, totalPointsRank, todayPointsRank)
            Spacer(modifier = Modifier.height(24.dp))
            PlayOrViewScoreButton(user.questionsAttemptedToday, onNavigateToQuiz, onNavigateToSummary)
        }
    }
}

@Composable
private fun StreakCard(user: User) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.fire__1_),
                contentDescription = "Streak",
                tint = Color(0xFFFF7700),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "${user.streak} Days",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@Composable
private fun StatsCard(
    user: User,
    streakRank: State<Int?>,
    totalPointsRank: State<Int?>,
    todayPointsRank: State<Int?>,
    ) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Stats",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            val accuracy = if (user.questionsAttemptedAllTime > 0) {
                (user.correctAnswersAllTime.toFloat() / user.questionsAttemptedAllTime * 100).toInt()
            } else 0
            StatsRow("Accuracy:", "$accuracy%")
            StatsRow("Average Points:", user.averagePoints.toInt().toString())
            StatsRow("Streak Ranking:", streakRank.value?.toString() ?: "-")
            StatsRow("Games Played:", user.gamesPlayedAllTime.toString())
            StatsRow("All-Time Ranking:", totalPointsRank.value?.toString() ?: "...")
            StatsRow("Today's Ranking:", todayPointsRank.value?.toString() ?: "...")

        }
    }
}

@Composable
private fun StatsRow(label: String, value: String) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = label,
            fontSize = 14.sp
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun PlayOrViewScoreButton(
    questionsAttempted: Int,
    onPlay: () -> Unit,
    onViewScore: () -> Unit
) {
    val (label, action) = if (questionsAttempted in -1..9) {
        "Play" to onPlay
    } else {
        "View Score" to onViewScore
    }

    Button(
        onClick = action,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0A2742)),
        modifier = Modifier
            .fillMaxWidth(0.5f)
            .height(48.dp)
    ) {
        Text(text = label, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}



@Composable
fun BottomNavBar() {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(12.dp)
    ) {
        /*NavBarItem(R.drawable.ic_home, "Home", true)
        NavBarItem(R.drawable.ic_leaderboard, "Leaderboard", false)
        NavBarItem(R.drawable.ic_profile, "Profile", false)
        NavBarItem(R.drawable.ic_settings, "Settings", false)*/
    }
}

@Composable
fun NavBarItem(iconRes: Int, label: String, isSelected: Boolean) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = label,
            tint = if (isSelected) Color(0xFF0A2742) else Color.Gray,
            modifier = Modifier.size(24.dp)
        )
        // Comment: In a real app, we would add onClick handlers for navigation
    }
}

//@Preview(showBackground = true)
//@Composable
//fun HomeScreenPreview() {
//    TopTenTriviaTheme {
//        HomeScreen("andrewwt97")
//    }
//}

