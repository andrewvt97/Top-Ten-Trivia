package com.example.question_screen

import android.os.Bundle
import android.os.CountDownTimer
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

@Composable
fun QuizApp() {

    //handles
    val navController = rememberNavController()
    val quizViewModel: QuizViewModel = viewModel()

    //by default, when the app launches, the composable with "quiz" gets called first.
    //TODO "home" should be the startDestination in the future
    NavHost(navController = navController, startDestination = "quiz") {
        composable("quiz") {
            QuizScreen(navController, quizViewModel)
        }
        composable("summary") {
            SummaryScreen(navController, quizViewModel)
        }
    }
}

@Composable
fun QuizScreen(navController: NavController, viewModel: QuizViewModel) { //QuizViewModel is a class defined on the bottom of page

    val scope = rememberCoroutineScope()

    val currentQuestionIndex = viewModel.currentQuestionIndex.value
    val score = viewModel.score.value
    val selectedOption = viewModel.selectedOption.value
    val remainingTime = viewModel.remainingTime.value
    val answeredQuestion = viewModel.answeredCurrentQuestion.value

    LaunchedEffect(currentQuestionIndex, answeredQuestion) {
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { /* Exit functionality would go here */ },
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
                        .fillMaxWidth((currentQuestionIndex + 1).toFloat() / viewModel.questions.size)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFF00416A))
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = "${currentQuestionIndex + 1}/${viewModel.questions.size}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Score",
                    tint = Color(0xFF00416A)
                )
                Text(
                    text = "$score",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00416A)
                )
            }
        }

        // Timer display
        Text(
            text = String.format("%.1f", remainingTime),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // Question card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF00416A),
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text( //TO-DO delete when questions are not only quotes
                    text = "Who does the following quote belong to?",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    text = "\"${viewModel.questions[currentQuestionIndex].quote}\"",
                    fontSize = 18.sp,
                    fontStyle = FontStyle.Italic,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }

        // Answer options
        viewModel.questions[currentQuestionIndex].options.forEachIndexed { index, option ->
            val optionLetter = when (index) {
                0 -> "A"
                1 -> "B"
                2 -> "C"
                3 -> "D"
                else -> (index + 65).toChar().toString()
            }

            // Highlight the selected option
            val isSelected = selectedOption == index
            val buttonColor = when {
                isSelected -> Color(0xFF3498DB) // Selected color (blue)
                else -> Color.White
            }
            val textColor = when {
                isSelected -> Color.White
                else -> Color.Black
            }

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
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonColor
                ),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = optionLetter,
                        color = textColor,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                    Text(
                        text = option,
                        color = textColor,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Next button
        Button(
            onClick = {
                if (answeredQuestion) {
                    if (currentQuestionIndex < viewModel.questions.size - 1) {
                        viewModel.moveToNextQuestion()
                    } else {
                        navController.navigate("summary")
                    }
                } else if (selectedOption != -1) {
                    viewModel.answerQuestion()

                    scope.launch {
                        delay(1000) // Wait a moment to show the answer
                        if (currentQuestionIndex < viewModel.questions.size - 1) {
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
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF00416A)
            ),
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
fun SummaryScreen(navController: NavController, viewModel: QuizViewModel) {
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
            text = "You answered $correctAnswers out of ${viewModel.questions.size} questions correctly.",
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

class QuizViewModel : ViewModel() {
    // Question data class
    data class Question(
        val quote: String,
        val options: List<String>,
        val correctOptionIndex: Int
    )

    // List of quiz questions
    val questions = listOf(
        Question(
            "Victory is reserved for those who are willing to pay its price.",
            listOf("Sun Tzu", "Napoléon", "Mark Anthony", "Kobe Bryant"),
            3 // Kobe Bryant
        ),
        Question(
            "The supreme art of war is to subdue the enemy without fighting.",
            listOf("Sun Tzu", "Alexander the Great", "Genghis Khan", "Julius Caesar"),
            0 // Sun Tzu
        ),
        Question(
            "I came, I saw, I conquered.",
            listOf("Alexander the Great", "Julius Caesar", "Napoleon Bonaparte", "Hannibal"),
            1 // Julius Caesar
        ),
        Question(
            "Be formless, shapeless, like water.",
            listOf("Bruce Lee", "Lao Tzu", "Confucius", "Jackie Chan"),
            0 // Bruce Lee
        ),
        Question(
            "It always seems impossible until it's done.",
            listOf("Martin Luther King Jr.", "Mahatma Gandhi", "Nelson Mandela", "Barack Obama"),
            2 // Nelson Mandela
        ),
        Question(
            "The best revenge is massive success.",
            listOf("Frank Sinatra", "Steve Jobs", "Albert Einstein", "Winston Churchill"),
            0 // Frank Sinatra
        ),
        Question(
            "Ask not what your country can do for you; ask what you can do for your country.",
            listOf("Abraham Lincoln", "John F. Kennedy", "Theodore Roosevelt", "Franklin D. Roosevelt"),
            1 // John F. Kennedy
        ),
        Question(
            "The only thing we have to fear is fear itself.",
            listOf("Winston Churchill", "Theodore Roosevelt", "Franklin D. Roosevelt", "Harry Truman"),
            2 // Franklin D. Roosevelt
        ),
        Question(
            "Success is not final, failure is not fatal: It is the courage to continue that counts.",
            listOf("Abraham Lincoln", "Winston Churchill", "Thomas Edison", "Henry Ford"),
            1 // Winston Churchill
        ),
        Question(
            "You miss 100% of the shots you don't take.",
            listOf("Michael Jordan", "LeBron James", "Wayne Gretzky", "Kobe Bryant"),
            2 // Wayne Gretzky
        )
    )

    // Timer
    private var countDownTimer: CountDownTimer? = null
    private val _remainingTime = mutableFloatStateOf(10.0f)
    val remainingTime: State<Float> = _remainingTime

    // State variables
    private val _currentQuestionIndex = mutableStateOf(0)
    val currentQuestionIndex: State<Int> = _currentQuestionIndex

    private val _score = mutableStateOf(0)
    val score: State<Int> = _score

    private val _selectedOption = mutableStateOf(-1)
    val selectedOption: State<Int> = _selectedOption

    private val _answeredCurrentQuestion = mutableStateOf(false)
    val answeredCurrentQuestion: State<Boolean> = _answeredCurrentQuestion

    private val _correctAnswers = mutableStateOf(0)
    val correctAnswers: State<Int> = _correctAnswers



    fun startTimer() {
        _remainingTime.floatValue = 10.0f
        countDownTimer?.cancel()

        countDownTimer = object : CountDownTimer(10000, 100) {
            override fun onTick(millisUntilFinished: Long) {
                _remainingTime.floatValue = millisUntilFinished / 1000.0f
            }

            override fun onFinish() {
                _remainingTime.floatValue = 0f
                if (!_answeredCurrentQuestion.value) {
                    answerQuestion()
                }
            }
        }.start()
    }

    fun selectOption(index: Int) {
        if (!_answeredCurrentQuestion.value) {
            _selectedOption.value = index
        }
    }

    fun answerQuestion() {
        countDownTimer?.cancel()
        _answeredCurrentQuestion.value = true

        val currentQuestion = questions[_currentQuestionIndex.value]
        if (_selectedOption.value == currentQuestion.correctOptionIndex) {
            // Calculate points based on remaining time
            val timePoints = (_remainingTime.floatValue * 10).toInt()
            _score.value += timePoints
            _correctAnswers.value += 1
        }
    }

    fun moveToNextQuestion() {
        if (_currentQuestionIndex.value < questions.size - 1) {
            _currentQuestionIndex.value += 1
            _selectedOption.value = -1
            _answeredCurrentQuestion.value = false
            startTimer()
        }
    }

    fun resetQuiz() {
        countDownTimer?.cancel()
        _currentQuestionIndex.value = 0
        _score.value = 0
        _selectedOption.value = -1
        _answeredCurrentQuestion.value = false
        _correctAnswers.value = 0
        _remainingTime.floatValue = 10.0f
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