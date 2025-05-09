package com.example.toptentrivia.model

import com.google.gson.annotations.SerializedName

data class TriviaResponse(
    val results:List<TriviaQuestions>
)
data class TriviaQuestions(
    val type: String,
    val difficulty: String,
    val category: String,
    val question: String,
    @SerializedName("correct_answer") val correctAnswer: String,
    @SerializedName("incorrect_answers") val incorrectAnswers: List<String>
)
