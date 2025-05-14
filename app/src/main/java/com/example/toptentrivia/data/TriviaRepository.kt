package com.example.toptentrivia.data

import com.example.toptentrivia.network.model.TriviaQuestions
import com.example.toptentrivia.network.TriviaApiService

interface TriviaRepository {
    suspend fun getTriviaQuestions(amount: Int = 10): List<TriviaQuestions>
}

class NetworkTriviaRepository(
    private val triviaApiService: TriviaApiService
) : TriviaRepository {
    override suspend fun getTriviaQuestions(amount: Int): List<TriviaQuestions> {
        return triviaApiService.getTrivia(amount = amount).results
    }
}
