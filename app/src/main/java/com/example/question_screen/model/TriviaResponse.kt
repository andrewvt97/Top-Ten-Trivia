package com.example.question_screen.model

data class TriviaResponse(
    val results:List<TriviaQuestions>
)
data class TriviaQuestions(
    val type: String,
    val difficulty: String,
    val category: String,
    val question: String,
    val correctAnswer: String,
    val incorrectAnswers: List<String>
)
