package com.example.movieapp.ui.details

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.movieapp.model.data.MoviesData
import com.example.movieapp.repository.MoviesRepository
import com.google.gson.JsonObject
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class DetailsFragmentAndFavouritesFragmentViewModel(application: Application) :
    AndroidViewModel(application) {

    private val job = Job()

    private val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    private val uiScope: CoroutineScope = CoroutineScope(coroutineContext)

    private val _liveData = MutableLiveData<State>()
    val liveData: LiveData<State>
        get() = _liveData

    private var moviesRepository: MoviesRepository = MoviesRepository(application)

    fun getMovieById(movieId: Int) {
        uiScope.launch {
            _liveData.value = State.ShowLoading
            val list = withContext(Dispatchers.IO) {
                try {
                    val response = moviesRepository.getMovieByIdCoroutine(movieId)
                    if (response.isSuccessful) {
                        val result = response.body()
                        if (result != null) {
                            Log.d("Done", "Successfully got Movie by Id")
                        }
                        result
                    } else {
                        moviesRepository.getMovieById(movieId)
                    }
                } catch (e: Exception) {
                    moviesRepository.getMovieById(movieId)
                }
            }
            _liveData.value = State.HideLoading
            _liveData.value = State.MovieById(list as MoviesData)
        }
    }

    fun getFavouriteMovies(sessionId: String, page: Int = 1) {
        uiScope.launch {
            _liveData.value = State.ShowLoading
            val list = withContext(Dispatchers.IO) {
                try {
                    val response = moviesRepository.getFavMoviesCoroutine(sessionId, page)
                    if (response.isSuccessful) {
                        val result = response.body()
                        result?.movies?.forEach {
                            it.id.let { id ->
                                if (id?.let { movieId -> moviesRepository.getMovieById(movieId) } != null) {
                                    moviesRepository.updateFavMovie(1, id)
                                } else {
                                    moviesRepository.insertItem(it)
                                    if (id != null) {
                                        moviesRepository.updateFavMovie(1, id)
                                    }
                                }
                            }
                        }
                        result?.movies
                    } else {
                        moviesRepository.getFavMovies(1)
                    }
                } catch (e: Exception) {
                    moviesRepository.getFavMovies(1)
                }
            }
            _liveData.value = State.HideLoading
            _liveData.value = State.FavouriteMovies(list as ArrayList<MoviesData>)
        }
    }

    fun setFavouriteMovies(favourite: Boolean, sessionId: String, movieId: Int) {
        val body = JsonObject().apply {
            addProperty("media_type", "movie")
            addProperty("media_id", movieId)
            addProperty("favorite", favourite)
        }
        uiScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val response = moviesRepository.setMoveMarkCoroutine(sessionId, body)
                    if (response.isSuccessful) {
                        val fav: Int = if (favourite) {
                            1
                        } else {
                            0
                        }
                        moviesRepository.updateFavMovie(fav, movieId)
                    } else {
                        Log.e("Error", "Cannot mark as favourite")
                    }
                } catch (e: Exception) {
                    Log.e("Error", e.toString())
                }
            }
        }
    }

    sealed class State {
        object ShowLoading : State()
        object HideLoading : State()
        data class FavouriteMovies(val result: ArrayList<MoviesData>) : State()
        data class MovieById(val result: MoviesData) : State()
    }
}