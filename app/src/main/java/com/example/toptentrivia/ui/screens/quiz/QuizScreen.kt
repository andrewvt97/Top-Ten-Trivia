package com.example.toptentrivia.ui.screens.quiz

import android.text.Html
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.toptentrivia.R
import com.example.toptentrivia.network.model.TriviaQuestions
import com.example.toptentrivia.ui.AppViewModelProvider
import com.example.toptentrivia.ui.navigation.NavigationDestination
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object QuizDestination : NavigationDestination {
    override val route = "quiz"
    override val titleRes = R.string.quiz_title
}

@Composable
fun QuizScreen(
    navController: NavController,
    viewModel: QuizViewModel
) {
    val quizUiState = viewModel.quizUiState

    when (quizUiState) {
        is QuizUiState.Loading -> {
            LoadingScreen()
        }
        is QuizUiState.Error -> {
            ErrorScreen()
        }
        is QuizUiState.Success -> {
            val questions = (quizUiState).questions

            QuizContent(navController, viewModel, questions)
        }
    }
}

@Composable
fun QuizContent(
    navController: NavController,
    viewModel: QuizViewModel,
    questions: List<TriviaQuestions>
) {
    val scope = rememberCoroutineScope()

    val currentIndex = viewModel.currentQuestionIndex.value
    val currentQuestion = questions[currentIndex]

    val score = viewModel.score.value
    val selectedOption = viewModel.selectedOption.value
    val answeredQuestion = viewModel.answeredCurrentQuestion.value
    val remainingTime = viewModel.remainingTime.value

    //val options = remember(currentIndex) { viewModel.shuffledOptionList }  //alternative option
    val options = viewModel.shuffledOptionList

    // options recomputed only when currentIndex changes
    /*val options = remember(currentIndex) {
        (currentQuestion.incorrectAnswers + currentQuestion.correctAnswer)
            .map { decodeHtml(it) } // WAT DOES THIS DO
            .shuffled()
    }*/

    LaunchedEffect(currentIndex, answeredQuestion) {
        if (!answeredQuestion) {
            viewModel.startTimer()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // Top section with back button, progress bar, and score
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // back button
            IconButton(
                onClick = { /* Exit or back action */ },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }

            Spacer(modifier = Modifier.width(16.dp))

            // progress bar
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.LightGray)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth((currentIndex + 1).toFloat() / questions.size)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFF00416A))
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "${currentIndex + 1}/${questions.size}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.width(16.dp))

            // score
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Star, contentDescription = "Score", tint = Color(0xFF00416A))
                Text(
                    text = "$score",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00416A)
                )
            }
        }

        // timer
        Text(
            text = String.format("%.1f", remainingTime),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        // question card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF00416A)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "Question:",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // question
                Text(
                    text = decodeHtml(currentQuestion.question),           //decodeHtml call
                    fontSize = 18.sp,
                    fontStyle = FontStyle.Italic,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // answer choices
        options.forEachIndexed { index, option ->
            val isSelected = selectedOption == index
            val buttonColor = if (isSelected) Color(0xFF3498DB) else Color.White
            val textColor = if (isSelected) Color.White else Color.Black

            Button(
                onClick = {
                    if (!answeredQuestion) {
                        viewModel.selectOption(index) //updates viewmodel's "selectedOption"
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .shadow(
                        elevation = if (isSelected) 8.dp else 2.dp,
                        shape = RoundedCornerShape(8.dp)
                    ),
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(16.dp)
            ) {

                // choices
                Text(
                    text = option.toString(),
                    color = textColor,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // next button; submits selected choice
        Button(
            onClick = {
                // end of quiz behavior
                if (answeredQuestion) {
                    if (currentIndex < questions.size - 1) {
                        viewModel.moveToNextQuestion()
                    } else {
                        navController.navigate("summary")
                    }
                } else if (selectedOption != -1) { // check selectedOption
                    viewModel.answerQuestion()

                    scope.launch {
                        delay(1000)
                        if (currentIndex < questions.size - 1) {
                            viewModel.moveToNextQuestion()
                        } else {
                            navController.navigate("summary")
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00416A)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Next", fontSize = 16.sp)
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Next",
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }

}

fun decodeHtml(encoded: String): String {
    return Html.fromHtml(encoded, Html.FROM_HTML_MODE_LEGACY).toString()
}