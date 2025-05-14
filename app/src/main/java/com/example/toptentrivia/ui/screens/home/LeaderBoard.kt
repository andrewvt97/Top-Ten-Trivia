package com.example.toptentrivia.ui.screens.leaderboard

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.toptentrivia.R
import com.example.toptentrivia.ui.AppViewModelProvider
import com.example.toptentrivia.ui.navigation.NavigationDestination
import com.example.toptentrivia.ui.screens.UserViewModel

object LeaderboardDestination : NavigationDestination {
    override val route = "leaderboard"
    override val titleRes = R.string.leaderboard_title
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LeaderboardScreen(
    navController: NavController,
    viewModel: UserViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    LaunchedEffect(Unit) { viewModel.fetchTopUsersToday() }

    val users by viewModel.topUsersToday.collectAsState()
    val topThree = users.take(3)
    val others = users.drop(3)
    val blue = Color(0xFF00416A)

    Column(Modifier.fillMaxSize()) {

        /* ---------- BLUE TOP BANNER (title + podium) ---------- */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0A2742))
                .padding(vertical = 16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Today's Leaderboard",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(Modifier.height(16.dp))

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Bottom
                ) {
                    if (topThree.size > 1) PodiumProfile(topThree[1], 2)
                    if (topThree.isNotEmpty()) PodiumProfile(topThree[0], 1)
                    if (topThree.size > 2) PodiumProfile(topThree[2], 3)
                }
            }
        }

        /* ---------- LIST OF THE REMAINING PLAYERS ---------- */
        /* ---------- LIST OF THE REMAINING PLAYERS ---------- */
        Box( // Wrap the LazyColumn in a Box with gray background
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFEFEFEF)) // Gray background
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                itemsIndexed(others) { index, user ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        shape = RoundedCornerShape(50),
                        colors = CardDefaults.cardColors(containerColor = Color.White) // Set to white
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(12.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${index + 4}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Spacer(Modifier.width(8.dp))
                            Image(
                                painter = painterResource(R.drawable.default_profile_icon),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(Color.Transparent)
                            )
                            Spacer(Modifier.width(12.dp))
                            Text(
                                text = user.username,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "${user.pointsToday.toInt()} pts",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }

    }
}




@Composable
fun PodiumProfile(user: com.example.toptentrivia.data.model.User, rank: Int) {
    val size = when (rank) {
        1 -> 90.dp
        else -> 70.dp
    }

    val borderColor = when (rank) {
        1 -> Color(0xFFFFD700) // Gold
        2 -> Color(0xFFC0C0C0) // Silver
        3 -> Color(0xFFCD7F32) // Bronze
        else -> Color.Transparent
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        Card(
            shape = CircleShape,
            modifier = Modifier
                .size(size)
                .border(width = 4.dp, color = borderColor, shape = CircleShape),
            colors = CardDefaults.cardColors(containerColor = Color.LightGray)
        ) {
            Image(
                painter = painterResource(id = R.drawable.profile_picture),
                contentDescription = "User Profile",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            )
        }

        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = rank.toString(),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black
        )

        Text(
            text = user.username,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )

        Text(
            text = "${user.pointsToday.toInt()}",
            fontSize = 12.sp,
            color = Color.White
        )
    }
}
