package com.example.toptentrivia.data

import com.example.toptentrivia.network.TriviaApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppContainer {
    val triviaRepository: TriviaRepository
}

class DefaultAppContainer : AppContainer {
    private val BASE_URL = "https://opentdb.com/"

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

    private val retrofitService: TriviaApiService by lazy {
        retrofit.create(TriviaApiService::class.java)
    }

    override val triviaRepository: TriviaRepository by lazy {
        NetworkTriviaRepository(retrofitService)
    }
}