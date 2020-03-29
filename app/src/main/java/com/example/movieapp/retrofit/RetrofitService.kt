package com.example.movieapp.retrofit

import android.util.Log
import com.example.movieapp.model.MovieResponse
import com.example.movieapp.model.MoviesData
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

object RetrofitService  {

    private const val BASE_URL = "https://api.themoviedb.org/3/"
    private lateinit var movieApi: MovieApi
   fun getMovieApi(): MovieApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getOkHttp())
            .build()
        movieApi =  retrofit.create(MovieApi::class.java)
       return movieApi
    }
    private fun getOkHttp(): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(getLoggingInterceptor())
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
    @GET("movie/popular")
    fun getPopularMovies(
        @Query("api_key") apiKey: String = "88f972ac2b5f07d969202c8ffbaaaffa",
        @Query("page") page: Int
    ): Call<MovieResponse>

    @GET("movie/{movie_id}")
    fun getMovieById(@Path("movie_id") movieId: Int,
                     @Query("api_key") apiKey: String = "88f972ac2b5f07d969202c8ffbaaaffa")
            :Call<MoviesData>
}

//testing new branch 