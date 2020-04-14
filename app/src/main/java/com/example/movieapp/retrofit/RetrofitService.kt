package com.example.movieapp.retrofit

import android.util.Log
import com.example.movieapp.model.AccountInfo
import com.example.movieapp.model.MovieResponse
import com.example.movieapp.model.MoviesData
import com.example.movieapp.ui.favourites.FavMovie
import com.example.movieapp.ui.favourites.FavResponse
import com.google.gson.JsonObject
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit


object RetrofitService {

    private const val BASE_URL = "https://api.themoviedb.org/3/"
    private lateinit var movieApi: MovieApi
    private var retrofit: Retrofit? = null

    fun getMovieApi(): MovieApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getOkHttp())
            .build()
        movieApi = retrofit.create(
            MovieApi::class.java
        )
        return movieApi
    }

    private fun getOkHttp(): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(getLoggingInterceptor())
            .addInterceptor { chain ->
                val newUrl = chain.request().url
                    .newBuilder()
                    .addQueryParameter(
                        "api_key",
                        "88f972ac2b5f07d969202c8ffbaaaffa"
                    )
                    .build()
                val newRequest = chain.request()
                    .newBuilder()
                    .url(newUrl)
                    .build()
                chain.proceed(newRequest)
            }
        return okHttpClient.build()
    }

    private fun getLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor(logger = object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                Log.d("OkHttp", message)
            }
        }).apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }
}

interface MovieApi {

    @POST("authentication/token/validate_with_login")
    fun login(@Body body: JsonObject): Call<JsonObject>

    @POST("authentication/session/new")
    fun createSession(@Body body: JsonObject): Call<JsonObject>

    @GET("authentication/token/new")
    fun createRequestToken(): Call<JsonObject>

    @GET("account")
    fun getAccountId(@Query("session_id") sessionId: String): Call<AccountInfo>

    @GET("movie/popular")
    fun getPopularMovies(
        @Query("page") page: Int
    ): Call<MovieResponse>

    @GET("movie/{movie_id}/account_states")
    fun getMovieState(@Path("movie_id") id: Int, @Query("api_key") apiKey: String?, @Query("session_id") session: String?): Call<MoviesData?>?

    @GET("movie/now_playing")
    fun getNowPlayingMovie(
        @Query("page") page: Int
    ): Call<MovieResponse>

    @GET("movie/{movie_id}")
    fun getMovieById(
        @Path("movie_id") movieId: Int
    ): Call<MoviesData>

    @Headers("Content-Type:application/json; charset=UTF-8")
    @POST("account/9178480/favorite?api_key=88f972ac2b5f07d969202c8ffbaaaffa")
    fun addFavList(@Body movie: FavMovie, @Query("session_id") session: String?): Call<FavResponse>

    @GET("account/9178480/favorite/movies?api_key=88f972ac2b5f07d969202c8ffbaaaffa")
    fun getFavList(@Query("session_id") session: String?): Call<MovieResponse>
}


