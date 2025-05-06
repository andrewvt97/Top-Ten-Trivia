package com.example.question_screen.network
import com.example.question_screen.model.TriviaResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://opentdb.com/"
private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())

    .baseUrl(BASE_URL)
                                        .build()
interface TriviaApiService{
    @GET("api.php")
    suspend fun getTrivia(
        @Query("amount") amount:Int = 10,
        @Query("type") type:String = "multiple",
        @Query("difficulty") difficulty:String = "easy"
    ):TriviaResponse
}
object TriviaApi{
    val retrofitService: TriviaApiService by lazy{
        retrofit.create(TriviaApiService::class.java)
    }
}