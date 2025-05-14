package com.example.toptentrivia.ui.screens.quiz

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.toptentrivia.ui.screens.leaderboard.LeaderboardDestination
import com.example.toptentrivia.ui.screens.leaderboard.LeaderboardScreen

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.toptentrivia.R
import com.example.toptentrivia.data.model.User
import com.example.toptentrivia.ui.AppViewModelProvider
import com.example.toptentrivia.ui.navigation.NavigationDestination
import com.example.toptentrivia.ui.screens.UserViewModel
import com.example.toptentrivia.ui.screens.navbar.BottomNavBar
import com.example.toptentrivia.ui.screens.quiz.QuizUiState
import com.example.toptentrivia.ui.screens.quiz.QuizViewModel

object SummaryDestination : NavigationDestination {
    override val route = "summary"
    override val titleRes = R.string.summary_title
}

@Composable
fun SummaryScreen(
    navController: NavController,
    viewModel: QuizViewModel,
    userViewModel: UserViewModel
) {
    val user = userViewModel.user.collectAsState().value
    val correctAnswers = user?.correctAnswersToday ?: 0
    val score = user?.pointsToday?.toInt() ?: 0
    val totalQuestions = user?.questionsAttemptedToday ?: 0


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        /* ---------- TOP NAVBAR ---------- */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0A2742))
                .padding(16.dp)
        ) {
            Text(
                text = "Top Ten Trivia",
                color = Color.White,
                fontSize = 24.sp,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        /* ---------- SUMMARY CONTENT ---------- */
        if (totalQuestions == 0) {
            Text(
                text = "You haven't taken the quiz today!",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                color = Color(0xFF00416A),
                modifier = Modifier.padding(32.dp)
            )
        } else {
            Text(
                text = "Quiz Completed!",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Text(
                text = "You answered $correctAnswers out of $totalQuestions questions correctly.",
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "Final Score: $score",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00416A),
                modifier = Modifier.padding(bottom = 32.dp)
            )

            Button(
                onClick = {
                    navController.navigate("leaderboard") {
                        popUpTo("summary") { inclusive = true }
                        launchSingleTop = true
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00416A)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Leaderboard", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }


}

