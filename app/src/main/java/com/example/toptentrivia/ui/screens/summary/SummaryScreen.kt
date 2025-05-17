package com.example.toptentrivia.ui.screens.summary

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.toptentrivia.network.model.TriviaQuestions
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

    val questions = viewModel.questionsAndAnswers
    // Track index with state
    var currentIndex by remember { mutableStateOf(0) }

    //jia
    Column {
        SummaryTopBar(totalQuestions)
        SummaryContent(
            correctAnswers,
            score,
            totalQuestions,
            navController,
            questions,
            currentIndex,
            // a "callback" ... apparently
            onPrevious = {
                if (currentIndex > 0) {
                    currentIndex--
                }
            },
            onNext = {
                if (currentIndex < questions.size - 1) {
                    currentIndex++
                }
            }
        )
    }

    /*andrew code from presentation
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        *//* ---------- TOP NAVBAR ---------- *//*
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

        *//* ---------- SUMMARY CONTENT ---------- *//*
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
    }*/


}

@Composable
private fun SummaryTopBar(
    totalQuestions: Int
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF0A2742))
            .padding(vertical = 28.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Top Ten Trivia",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (totalQuestions == 0) {
                Text(
                    text = "You haven't taken the quiz today!",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    modifier = Modifier.padding(32.dp)
                )
            } else {
                Text(
                    text = "Quiz Completed!",
                    style = TextStyle(
                        fontSize = 25.sp,
                        color = Color.White,
                    ),
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Today's Trivia Results",
                    color = Color.White,
                    fontSize = 25.sp
                )
            }
        }
    }
}

@Composable
private fun SummaryContent(
    correctAnswers: Int,
    score: Int,
    totalQuestions: Int,
    navController: NavController,
    questions: List<TriviaQuestions>,
    currentIndex: Int,
    onPrevious: () -> Unit,
    onNext: () -> Unit
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        //Final Score Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Final Score",
                    fontSize = 35.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 5.dp),
                )

                Spacer(modifier = Modifier.padding(8.dp))

                // correct/10
                Text(
                    text = "Accuracy: $correctAnswers / $totalQuestions",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(start = 15.dp)
                )

                // points
                Text(
                    text = "Points: $score",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 15.dp, top = 5.dp)
                )

                Spacer(modifier = Modifier.padding(5.dp))
            }
        }

        // Questions slideshow section
        Box(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left navigation button
                IconButton(
                    onClick = onPrevious,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Previous Question",
                        modifier = Modifier.size(32.dp)
                    )
                }

                // Question Card with AnimatedContent for smooth transitions
                Card(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Questions",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        // Animated content for slide transitions between questions
                        AnimatedContent(
                            targetState = currentIndex,
                            transitionSpec = {
                                val direction = if (targetState > initialState)
                                    AnimatedContentTransitionScope.SlideDirection.Left
                                else
                                    AnimatedContentTransitionScope.SlideDirection.Right

                                slideIntoContainer(
                                    towards = direction,
                                    animationSpec = tween(300)
                                ) togetherWith slideOutOfContainer(
                                    towards = direction,
                                    animationSpec = tween(300)
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) { index ->
                            // Only render if we have questions
                            if (questions.isNotEmpty() && index < questions.size) {
                                Column {
                                    // Question text
                                    Text(
                                        text = "${index + 1}. ${questions[index].question}",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )

                                    // Correct answer with checkmark
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(top = 8.dp)
                                    ) {
                                        // Display the correct answer
                                        Text(
                                            text = "${questions[index].correctAnswer}",
                                            fontSize = 16.sp,
                                            modifier = Modifier.weight(1f)
                                        )

                                        // Green checkmark indicator
                                        Box(
                                            modifier = Modifier
                                                .size(24.dp)
                                                .background(
                                                    color = Color(0xFF004D40),
                                                    shape = RoundedCornerShape(4.dp)
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "✓",
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }

                                    // TODO display user choice
                                }
                            } else Text("No questions available")

                        }
                    }
                }

                // Right navigation button
                IconButton(
                    onClick = onNext,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "Next Question",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // leader board button (preserved from original code)
        Button(
            onClick = {
                navController.navigate("leaderboard") {
                    popUpTo("leaderboard") { inclusive = true }
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF00416A)
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Leaderboard", fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.padding(15.dp))
    }
}




