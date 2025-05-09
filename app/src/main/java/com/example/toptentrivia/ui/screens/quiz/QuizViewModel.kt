package com.example.toptentrivia.ui.screens.quiz

import android.os.CountDownTimer
import android.text.Html
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.toptentrivia.data.TriviaRepository
import com.example.toptentrivia.network.model.TriviaQuestions
import kotlinx.coroutines.launch

sealed interface QuizUiState {
    data class Success(val questions: List<TriviaQuestions>) : QuizUiState
    object Error : QuizUiState
    object Loading : QuizUiState
}

class QuizViewModel(
    private val triviaRepository: TriviaRepository
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

    init {
        getTrivia()
    }

    private fun getTrivia() {
        viewModelScope.launch {
            quizUiState = QuizUiState.Loading
            try {
                val triviaQuestions = triviaRepository.getTriviaQuestions()
                if (triviaQuestions.isNotEmpty()) {
                    quizUiState = QuizUiState.Success(triviaQuestions)
                } else {
                    quizUiState = QuizUiState.Error
                }
            } catch (e: Exception) {
                quizUiState = QuizUiState.Error
            }
        }
    }

    fun selectOption(index: Int) {
        if (!answeredCurrentQuestion.value) {
            selectedOption.value = index
        }
    }

    fun decodeHtml(encoded: String): String {
        return Html.fromHtml(encoded, Html.FROM_HTML_MODE_LEGACY).toString()
    }

    fun answerQuestion() {
        val questions = (quizUiState as? QuizUiState.Success)?.questions
        if (questions != null && currentQuestionIndex.value < questions.size) {
            answeredCurrentQuestion.value = true
            val current = questions[currentQuestionIndex.value]
            val allOptions = current.incorrectAnswers + current.correctAnswer
            val shuffled = allOptions.shuffled()
            val correct = current.correctAnswer
            if (selectedOption.value != -1) {
                val selected = shuffled[selectedOption.value]
                if (selected == correct) {
                    score.value += 10
                    correctAnswers.value += 1
                    println("Correct Answers: " + correctAnswers.value)
                }
            }
        }
    }

    fun moveToNextQuestion() {
        val questions = (quizUiState as? QuizUiState.Success)?.questions
        if (questions != null && currentQuestionIndex.value < questions.size - 1) {
            currentQuestionIndex.value += 1
            selectedOption.value = -1
            answeredCurrentQuestion.value = false
        }
    }

    fun startTimer() {
        _remainingTime.value = 10.0f
        countDownTimer?.cancel()

        countDownTimer = object : CountDownTimer(10000, 100) {
            override fun onTick(millisUntilFinished: Long) {
                _remainingTime.value = millisUntilFinished / 1000.0f
            }

            override fun onFinish() {
                _remainingTime.value = 0f
                if (!answeredCurrentQuestion.value) {
                    answerQuestion()
                }
            }
        }.start()
    }

    fun resetQuiz(){
        println("Correct Answers: " + correctAnswers.value)
        countDownTimer?.cancel()
        currentQuestionIndex.value=0
        selectedOption.value=-1
        answeredCurrentQuestion.value=false
        score.value=0
        correctAnswers.value=0
        _remainingTime.value=10.0f
    }


}
