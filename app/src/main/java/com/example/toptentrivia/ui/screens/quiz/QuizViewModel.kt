package com.example.toptentrivia.ui.screens.quiz

import android.os.CountDownTimer
import android.text.Html

import android.util.Log
import androidx.compose.runtime.State

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.toptentrivia.data.TriviaRepository
import com.example.toptentrivia.data.UserRepository
import com.example.toptentrivia.data.model.User
import com.example.toptentrivia.network.model.TriviaQuestions
import com.example.toptentrivia.ui.screens.UserViewModel
import kotlinx.coroutines.launch

sealed interface QuizUiState {
    data class Success(val questions: List<TriviaQuestions>) : QuizUiState
    object Error : QuizUiState
    object Loading : QuizUiState
}

class QuizViewModel(
    private val triviaRepository: TriviaRepository,
) : ViewModel() {



    private var countDownTimer: CountDownTimer? = null
    private val _remainingTime = mutableStateOf(10.0f)
    val remainingTime: State<Float> = _remainingTime

    var quizUiState: QuizUiState by mutableStateOf(QuizUiState.Loading)
        private set

    val currentQuestionIndex = mutableStateOf(0)
    val selectedOption = mutableStateOf(-1)
    val answeredCurrentQuestion = mutableStateOf(false)
    val score = mutableStateOf(0)
    val correctAnswers = mutableStateOf(0)



    fun getTrivia(userViewModel: UserViewModel) {
        // Launches a coroutine within the ViewModel’s lifecycle scope


        viewModelScope.launch {
            quizUiState = QuizUiState.Loading
            try {
                val triviaQuestions = triviaRepository.getTriviaQuestions()
                if (triviaQuestions.isNotEmpty()) {
                    quizUiState = QuizUiState.Success(triviaQuestions) //load questions from repo
                    userViewModel.incrementOnGameStart()

                } else {
                    quizUiState = QuizUiState.Error
                }
            } catch (e: Exception) {
                quizUiState = QuizUiState.Error
                Log.e("TriviaViewModel", "Failed to fetch trivia: ${e.localizedMessage}")
            }
        }
    }


    fun selectOption(index: Int) {
        if (!answeredCurrentQuestion.value) {
            selectedOption.value = index
        }
    }

    fun answerQuestion(questions: List<TriviaQuestions>, options: List<String>, userViewModel: UserViewModel) {
        if (selectedOption.value != -1 && currentQuestionIndex.value < questions.size) {
            val current = questions[currentQuestionIndex.value]

            val correct = current.correctAnswer
            val selected = options[selectedOption.value]
            if (selected == correct) {
                val timeLeft = remainingTime.value
                val points = timeLeft * 10.0
                /*val points = when {
                    timeLeft > 5f -> 25
                    timeLeft > 0f -> 15
                    else -> 10
                }*/
                score.value += points.toInt()
                correctAnswers.value += 1
                userViewModel.incrementScore(points)
            }
        }
    }


    fun moveToNextQuestion(userViewModel: UserViewModel) {

        val questions = (quizUiState as? QuizUiState.Success)?.questions

        if (questions != null && currentQuestionIndex.value < questions.size - 1) {
            userViewModel.incrementAttempt()
            currentQuestionIndex.value += 1
            selectedOption.value = -1
            answeredCurrentQuestion.value = false
        }
    }





    fun startTimer(userViewModel: UserViewModel) {


        _remainingTime.value = 10.0f
        countDownTimer?.cancel()

        countDownTimer = object : CountDownTimer(10000, 100) {
            override fun onTick(millisUntilFinished: Long) {
                _remainingTime.value = millisUntilFinished / 1000.0f
            }

            override fun onFinish() {
                _remainingTime.value = 0f
                if (!answeredCurrentQuestion.value) {
                    val questions = (quizUiState as? QuizUiState.Success)?.questions
                    if (questions != null) {
                        val current = questions[currentQuestionIndex.value]
                        val options = (current.incorrectAnswers + current.correctAnswer).shuffled()
                        answerQuestion(questions, options, userViewModel)
                    }
                }
            }
        }.start()
    }


    fun resetQuiz(userViewModel: UserViewModel) {

        countDownTimer?.cancel()
        currentQuestionIndex.value = 0
        selectedOption.value = -1
        answeredCurrentQuestion.value = false
        score.value = 0
        correctAnswers.value = 0
        _remainingTime.value = 10.0f
        getTrivia(userViewModel)
    }
}
