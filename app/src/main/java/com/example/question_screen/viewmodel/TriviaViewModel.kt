package com.example.question_screen.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.question_screen.model.TriviaQuestions
import com.example.question_screen.network.TriviaApi
import kotlinx.coroutines.launch
import androidx.compose.runtime.State

class TriviaViewModel:ViewModel() {
    private val _questions = mutableStateOf<List<TriviaQuestions>>(emptyList())



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
}