package com.example.movieapp.repository

import android.app.Application
import android.util.Log
import com.example.movieapp.model.data.MovieResponse
import com.example.movieapp.model.data.MoviesData
import com.example.movieapp.model.database.MoviesDao
import com.example.movieapp.model.database.MoviesDatabase
import com.example.movieapp.model.network.RetrofitService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

class MoviesRepository(application: Application): CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private var moviesDao: MoviesDao? = null
    private var retrofit = RetrofitService.getMovieApi()

    init {
        val db = MoviesDatabase.getDatabase(application)
        moviesDao = db?.moviesDao()
    }

    fun getMovieById(movieId: Int): MoviesData? {
        return moviesDao?.getMovieById(movieId)
    }

    fun getPopularMovies(popularMovies: Int): List<MoviesData>? {
        return moviesDao?.getPopularMovies(popularMovies)
    }

    fun getNowPlayingMovies(nowPlayingMovies: Int): List<MoviesData>? {
        return moviesDao?.getNowPlayingMovies(nowPlayingMovies)
    }

    suspend fun insertListBG(moviesData: List<MoviesData>) {
        withContext(Dispatchers.IO) {
            moviesDao?.insertList(moviesData)
        }
    }

    fun insertItem(moviesData: MoviesData) {
        launch {
            insetItemBG(moviesData)
        }
    }

    suspend fun insetItemBG(moviesData: MoviesData) {
        withContext(Dispatchers.IO) {
            moviesDao?.insertItem(moviesData)
        }
    }

    suspend fun updateFavMovieBG(favourite: Int, movieId: Int) {
        withContext(Dispatchers.IO) {
            Log.d("Dead inside", favourite.toString())
            moviesDao?.updateFavMovie(favourite, movieId)
        }
    }

    fun updatePopularMovies(popularMovie: Int, movieId: Int) {
        launch {
            updatePopularMoviesBG(popularMovie, movieId)
        }
    }

    suspend fun updatePopularMoviesBG(popularMovie: Int, movieId: Int) {
        withContext(Dispatchers.IO) {
            moviesDao?.updatePopularMovie(popularMovie, movieId)
        }
    }

    fun updateNowPlayingMovies(nowPlayingMovies: Int, movieId: Int) {
        launch {
            updateNowPlayingMoviesBG(nowPlayingMovies, movieId)
        }
    }

    suspend fun updateNowPlayingMoviesBG(nowPlayingMovies: Int, movieId: Int) {
        withContext(Dispatchers.IO) {
            moviesDao?.updateNowPlayingMovies(nowPlayingMovies, movieId)
        }
    }

    suspend fun getPopularMoviesCoroutine(page: Int): Response<MovieResponse> {
        return retrofit.getPopularMovies(page)
    }

    suspend fun getNowPlayingMovesCoroutine(page: Int): Response<MovieResponse> {
        return retrofit.getNowPlayingMovies(page)
    }

}