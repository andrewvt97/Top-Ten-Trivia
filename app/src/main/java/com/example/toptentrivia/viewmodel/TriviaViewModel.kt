package com.example.toptentrivia.viewmodel

import android.os.CountDownTimer
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.toptentrivia.model.TriviaQuestions
import com.example.toptentrivia.network.TriviaApi
import kotlinx.coroutines.launch
import androidx.compose.runtime.State
import android.text.Html

class TriviaViewModel:ViewModel() {
    private val _questions = mutableStateOf<List<TriviaQuestions>>(emptyList())
    private var countDownTimer: CountDownTimer? = null
    private val _remainingTime = mutableStateOf(10.0f)
    val remainingTime: State<Float> = _remainingTime



    val questions: State<List<TriviaQuestions>> = _questions

    init {
        getTrivia()
    }
    private fun getTrivia(){
        viewModelScope.launch {
            try {
                val response = TriviaApi.retrofitService.getTrivia()
                _questions.value = response.results
            } catch (e: Exception) {

            }
        }
    }
    val currentQuestionIndex = mutableStateOf(0)
    val selectedOption = mutableStateOf(-1)
    val answeredCurrentQuestion = mutableStateOf(false)
    val score = mutableStateOf(0)
    val correctAnswers = mutableStateOf(0)

    fun selectOption(index: Int) {
        if (!answeredCurrentQuestion.value) {
            selectedOption.value = index
        }
    }
    fun decodeHtml(encoded: String): String {
        return Html.fromHtml(encoded, Html.FROM_HTML_MODE_LEGACY).toString()
    }

    fun answerQuestion() {
        answeredCurrentQuestion.value = true
        val current = questions.value[currentQuestionIndex.value]
        val allOptions = current.incorrectAnswers + current.correctAnswer
        val shuffled = allOptions.shuffled()
        val correct = current.correctAnswer
        if (selectedOption.value != -1) {
            val selected = shuffled[selectedOption.value]
            if (selected == correct) {
                score.value += 10
                correctAnswers.value += 1
            }
        }

    }

    fun moveToNextQuestion() {
        if (currentQuestionIndex.value < questions.value.size - 1) {
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

    fun resetQuiz() {
        countDownTimer?.cancel()
        currentQuestionIndex.value = 0
        selectedOption.value = -1
        answeredCurrentQuestion.value = false
        score.value = 0
        correctAnswers.value = 0
        _remainingTime.value = 10.0f
    }


}