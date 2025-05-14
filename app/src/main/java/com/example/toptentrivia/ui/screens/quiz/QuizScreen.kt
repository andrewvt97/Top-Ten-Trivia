package com.example.toptentrivia.ui.screens.quiz

import android.text.Html
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
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
import androidx.navigation.NavController
import com.example.toptentrivia.R
import com.example.toptentrivia.network.model.TriviaQuestions
import com.example.toptentrivia.ui.navigation.NavigationDestination
import com.example.toptentrivia.ui.screens.UserViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object QuizDestination : NavigationDestination {
    override val route = "quiz"
    override val titleRes = R.string.quiz_title
}

@Composable
fun QuizScreen(
    navController: NavController,
    userViewModel: UserViewModel,
    viewModel: QuizViewModel
) {

    val user = userViewModel.user.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getTrivia(userViewModel)
    }

    val quizUiState = viewModel.quizUiState

    when (quizUiState) {
        is QuizUiState.Loading -> {
            LoadingScreen()
        }
        is QuizUiState.Error -> {
            ErrorScreen()
        }
        is QuizUiState.Success -> {
            val questions = quizUiState.questions
            QuizContent(navController, viewModel, userViewModel, questions)

        }
    }
}

@Composable
fun QuizContent(
    navController: NavController,
    viewModel: QuizViewModel,
    userViewModel: UserViewModel,
    questions: List<TriviaQuestions>
) {
    val scope = rememberCoroutineScope()

    val currentIndex = viewModel.currentQuestionIndex.value
    val currentQuestion = questions[currentIndex]

    val score = viewModel.score.value
    val selectedOption = viewModel.selectedOption.value
    val answeredQuestion = viewModel.answeredCurrentQuestion.value
    val remainingTime = viewModel.remainingTime.value

    val options = remember(currentIndex) {
        (currentQuestion.incorrectAnswers + currentQuestion.correctAnswer)
            .map { decodeHtml(it) }
            .shuffled()
    }

    LaunchedEffect(currentIndex, answeredQuestion) {
        if (!answeredQuestion) {
            viewModel.startTimer(userViewModel)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {

        // Top bar: Back, Progress bar, Score
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            IconButton(onClick = { /* Optional back action */ }) {
                Icon(
                    Icons.Filled.Close, contentDescription = "Back")
            //Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }

            //Spacer(modifier = Modifier.width(16.dp))

            //progress bar
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(13.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray)
            ) {
                //inner progress bar
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth((currentIndex + 1).toFloat() / questions.size)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFF00416A))
                )
            }

            //Spacer(modifier = Modifier.width(2.dp))

            // progress text
            val progress = viewModel.startingAttempts + currentIndex + 1

            Text(
                text = "$progress/10",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )



            Spacer(modifier = Modifier.width(10.dp))

            // score
            Row(
                modifier = Modifier.padding(end = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Star, contentDescription = "Score", tint = Color(0xFFFFE04B))
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
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 16.dp)
        )

        // Question Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF00416A)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Question:",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    text = decodeHtml(currentQuestion.question),
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Answer options
        Column(
            modifier = Modifier
                .fillMaxWidth()
                //.weight(1f)
                .padding(top = 80.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ){
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
                        .shadow(
                            elevation = if (isSelected) 16.dp else 4.dp,
                            shape = RoundedCornerShape(8.dp)
                        ),
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = when (index) {
                                0->"A"
                                1->"B"
                                2->"C"
                                3->"D"
                                else -> "index not found"
                            },
                            color = textColor,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = option,
                                fontSize = 18.sp,
                                color = textColor,
                            )
                        }

                    }
                }
            }
        }
        /*options.forEachIndexed { index, option ->
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
        }*/

        //Spacer(modifier = Modifier.weight(1f))

        // Next button
        Button(
            onClick = {
                if (answeredQuestion) {
                    if (currentIndex < questions.size - 1) {
                        viewModel.moveToNextQuestion(userViewModel)
                    } else {
                        userViewModel.updateAverage()
                        navController.navigate("summary")
                    }

                } else if (selectedOption != -1) { // check selectedOption
                    viewModel.answerQuestion(questions, options, userViewModel)


                    scope.launch {
                        delay(1000)
                        if (currentIndex < questions.size - 1) {
                            viewModel.moveToNextQuestion(userViewModel)
                        } else {
                            userViewModel.updateAverage()
                            navController.navigate("summary")
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 60.dp),
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
