package com.example.toptentrivia.data

import android.content.Context
import com.example.toptentrivia.data.model.User
import com.example.toptentrivia.network.TriviaApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppContainer {
    val triviaRepository: TriviaRepository
    val userRepository: UserRepository

}

class DefaultAppContainer(private val context: Context) : AppContainer {
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


    // singleton for Room database
    private val database = AppDatabase.getDatabase(context)

    // initializing object for database operations
    override val userRepository: UserRepository by lazy {
        OfflineUserRepository(database.userDao())
    }



}