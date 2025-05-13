package com.example.toptentrivia.ui.screens.quiz

import android.os.CountDownTimer
import android.text.Html
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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

    // can only be either loading, error, or Success, initially starts as loading
    var quizUiState: QuizUiState by mutableStateOf(QuizUiState.Loading)
        private set

    val currentQuestionIndex = mutableStateOf(0)
    val selectedOption = mutableStateOf(-1)
    val answeredCurrentQuestion = mutableStateOf(false)
    val score = mutableStateOf(0)
    val correctAnswers = mutableStateOf(0)

    //var shuffledOptionList = mutableStateListOf()

    var shuffledOptionList: List<String> = listOf(

    )

//    init {
//        getTrivia()
//    }

    //
    fun getTrivia(userViewModel: UserViewModel) {
        // Launches a coroutine within the ViewModel’s lifecycle scope
        viewModelScope.launch {
            quizUiState = QuizUiState.Loading
            try {
                val triviaQuestions = triviaRepository.getTriviaQuestions()
                if (triviaQuestions.isNotEmpty()) {
                    quizUiState = QuizUiState.Success(triviaQuestions) //load questions from repo
                    shuffleOptionList()
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

    /*fun shuffleOptions(): List<String> {
        var shuffledOptions: List<String> = listOf()
        val questions = (quizUiState as? QuizUiState.Success)?.questions
        if (questions != null && currentQuestionIndex.value < questions.size) {
            val current = questions[currentQuestionIndex.value]
            shuffledOptions = (current.incorrectAnswers + current.correctAnswer).shuffled()
        }
        return shuffledOptions
    }*/

    fun shuffleOptionList(){
        val questions = (quizUiState as? QuizUiState.Success)?.questions
        if (questions != null && currentQuestionIndex.value < questions.size) {
            val current = questions[currentQuestionIndex.value]
            shuffledOptionList = (current.incorrectAnswers + current.correctAnswer).shuffled()
        }
    }

    fun answerQuestion(userViewModel: UserViewModel) {
        // can cast quizUiState to Success? if yes{ did cast succeed?: yes(can do ".questions")}

        val questions = (quizUiState as? QuizUiState.Success)?.questions


        if (questions != null && currentQuestionIndex.value < questions.size) {
            val current = questions[currentQuestionIndex.value]
            /*answeredCurrentQuestion.value = true
            val allOptions: List<String> = current.incorrectAnswers + current.correctAnswer
            val shuffled = allOptions.shuffled()*/
            val correct = current.correctAnswer
            if (selectedOption.value != -1) {
                val selected = shuffledOptionList[selectedOption.value].toString()
                if (selected == correct) {
                    score.value += 10
                    correctAnswers.value += 1
                    userViewModel.incrementScore(10.0) // change this to timed answer later

                    println("Correct Answers: " + correctAnswers.value)
                }
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
        shuffleOptionList()
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
                    answerQuestion(userViewModel)
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
