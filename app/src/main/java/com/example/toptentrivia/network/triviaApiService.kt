package com.example.toptentrivia.network
import com.example.toptentrivia.network.model.TriviaResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TriviaApiService {
    @GET("api.php")
    suspend fun getTrivia(
        @Query("amount") amount: Int = 10,
        @Query("type") type: String = "multiple",
        @Query("difficulty") difficulty: String = "easy"
    ): TriviaResponse
}

//private const val BASE_URL = "https://opentdb.com/"
//private val retrofit = Retrofit.Builder()
//    .addConverterFactory(GsonConverterFactory.create())
//
//    .baseUrl(BASE_URL)
//    .build()
//interface TriviaApiService{
//    @GET("api.php")
//    suspend fun getTrivia(
//        @Query("amount") amount:Int = 10,
//        @Query("type") type:String = "multiple",
//        @Query("difficulty") difficulty:String = "easy"
//    ):TriviaResponse
//}
//object TriviaApi{
//    val retrofitService: TriviaApiService by lazy{
//        retrofit.create(TriviaApiService::class.java)
//    }
//}

