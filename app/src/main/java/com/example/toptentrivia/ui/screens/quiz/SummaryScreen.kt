package com.example.toptentrivia.ui.screens.quiz

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.toptentrivia.R
import com.example.toptentrivia.ui.AppViewModelProvider
import com.example.toptentrivia.ui.navigation.NavigationDestination
import com.example.toptentrivia.ui.screens.quiz.QuizUiState
import com.example.toptentrivia.ui.screens.quiz.QuizViewModel

object SummaryDestination : NavigationDestination {
    override val route = "summary"
    override val titleRes = R.string.summary_title
}

@Composable
fun SummaryScreen(
    navController: NavController,
     viewModel: QuizViewModel
) {
    val score = viewModel.score.value
    val correctAnswers = viewModel.correctAnswers.value
    val quizUiState = viewModel.quizUiState

    val totalQuestions = when (quizUiState) {
        is QuizUiState.Success -> quizUiState.questions.size
        else -> 0
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
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
                viewModel.resetQuiz()
                navController.navigate("quiz") {
                    popUpTo("quiz") { inclusive = true }
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF00416A)
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Start New Quiz", fontSize = 16.sp)
        }
    }
}
