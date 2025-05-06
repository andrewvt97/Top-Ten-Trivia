package com.example.question_screen

import android.os.Bundle
import android.os.CountDownTimer
import android.text.Html
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.question_screen.viewmodel.TriviaViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuizAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    QuizApp()
                }
            }
        }
    }
}
//this is to display Quotations in the questions
fun decodeHtml(encoded: String): String {
    return Html.fromHtml(encoded, Html.FROM_HTML_MODE_LEGACY).toString()
}

@Composable
fun QuizApp() {
    val navController = rememberNavController()
    val viewModel: TriviaViewModel = viewModel() // Only use this now

    val triviaQuestions = viewModel.questions.value

    if (triviaQuestions.isNotEmpty()) {
        NavHost(navController = navController, startDestination = "quiz") {
            composable("quiz") {
                QuizScreen(navController, viewModel)
            }
            composable("summary") {
                SummaryScreen(navController, viewModel)
            }
        }
    } else {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}


@Composable
fun QuizScreen(navController: NavController, viewModel: TriviaViewModel) {
    val scope = rememberCoroutineScope()

    val currentIndex = viewModel.currentQuestionIndex.value
    val currentQuestion = viewModel.questions.value[currentIndex]
    val score = viewModel.score.value
    val selectedOption = viewModel.selectedOption.value
    val answeredQuestion = viewModel.answeredCurrentQuestion.value
    val remainingTime = viewModel.remainingTime.value

    // Shuffle options only once per question
    val options = remember(currentIndex) {
        (currentQuestion.incorrectAnswers + currentQuestion.correctAnswer)
            .map { decodeHtml(it) }
            .shuffled()
    }


    // Timer
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
        // Top Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { /* Exit or back action */ },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }

            Spacer(modifier = Modifier.width(16.dp))

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
                        .fillMaxWidth((currentIndex + 1).toFloat() / viewModel.questions.value.size)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFF00416A))
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = "${currentIndex + 1}/${viewModel.questions.value.size}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.width(16.dp))

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

        // Timer
        Text(
            text = String.format("%.1f", remainingTime),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        // Question Card
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

                Text(
                    text = decodeHtml(currentQuestion.question),
                    fontSize = 18.sp,
                    fontStyle = FontStyle.Italic,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Options
        options.forEachIndexed { index, option ->
            val isSelected = selectedOption == index
            val buttonColor = if (isSelected) Color(0xFF3498DB) else Color.White
            val textColor = if (isSelected) Color.White else Color.Black

            Button(
                onClick = {
                    if (!answeredQuestion) {
                        viewModel.selectOption(index)
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
                Text(
                    text = option,
                    color = textColor,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Next button
        Button(
            onClick = {
                if (answeredQuestion) {
                    if (currentIndex < viewModel.questions.value.size - 1) {
                        viewModel.moveToNextQuestion()
                    } else {
                        navController.navigate("summary")
                    }
                } else if (selectedOption != -1) {
                    viewModel.answerQuestion()

                    scope.launch {
                        delay(1000)
                        if (currentIndex < viewModel.questions.value.size - 1) {
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


@Composable
fun SummaryScreen(navController: NavController, viewModel: TriviaViewModel) {
    val score = viewModel.score.value
    val correctAnswers = viewModel.correctAnswers.value

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
            text = "You answered $correctAnswers out of ${viewModel.questions.value.size} questions correctly.",
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



@Composable
fun QuizAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF00416A),
            secondary = Color(0xFF3498DB),
            background = Color(0xFFF5F5F5),
            surface = Color.White
        ),
        content = content
    )
}