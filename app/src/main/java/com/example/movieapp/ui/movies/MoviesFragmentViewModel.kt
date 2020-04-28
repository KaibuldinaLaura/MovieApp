package com.example.movieapp.ui.movies

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.movieapp.model.data.MoviesData
import com.example.movieapp.repository.MoviesRepository
import kotlinx.coroutines.*
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class MoviesFragmentViewModel(application: Application) : AndroidViewModel(application) {

    private val job = Job()

    private val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    private val uiScope: CoroutineScope = CoroutineScope(coroutineContext)

    private val _liveData = MutableLiveData<State>()
    val liveData: LiveData<State>
        get() = _liveData

    private var moviesRepository: MoviesRepository = MoviesRepository(application)

    fun getPopularMovies(
        page: Int = 1
    ) {
        uiScope.launch {
            if (page == 1) {
                _liveData.value = State.ShowLoading
            }
            val list = withContext(Dispatchers.IO) {
                try {
                    val response = moviesRepository.getPopularMoviesCoroutine(page)
                    if (response.isSuccessful) {
                        val result = response.body()
                        result?.movies?.forEach {
                            it.id?.let { id ->
                                if (moviesRepository.getMovieById(id) != null) {
                                    moviesRepository.updatePopularMovies(1, id)
                                } else {
                                    moviesRepository.insertItem(it)
                                    moviesRepository.updatePopularMovies(1, id)
                                }
                            }
                        }
                        result?.movies
                    } else {
                        moviesRepository.getPopularMovies(1) ?: emptyList()
                    }
                } catch (e: Exception) {
                    moviesRepository.getPopularMovies(1) ?: emptyList()
                }
            }
            _liveData.value = State.HideLoading
            _liveData.value = State.PopularMovies(
                result = list as ArrayList<MoviesData>
            )
        }
    }

    fun getNowPlayingMovies(
        page: Int = 1
    ) {
        uiScope.launch {
            if (page == 1) {
                _liveData.value = State.ShowLoading
            }
            val list = withContext(Dispatchers.IO) {
                try {
                    val response = moviesRepository.getNowPlayingMovesCoroutine(page)
                    if (response.isSuccessful) {
                        val result = response.body()
                        result?.movies?.forEach {
                            it.id?.let { id ->
                                if (moviesRepository.getMovieById(id) != null) {
                                    moviesRepository.updateNowPlayingMovies(1, id)
                                } else {
                                    moviesRepository.insertItem(it)
                                    moviesRepository.updateNowPlayingMovies(1, id)
                                }
                            }
                        }
                        result?.movies
                    } else {
                        moviesRepository.getNowPlayingMovies(1) ?: emptyList()
                    }
                } catch (e: Exception) {
                    moviesRepository.getNowPlayingMovies(1) ?: emptyList()
                }
            }
            _liveData.value = State.HideLoading
            _liveData.value = State.NowPlayingMovies(
                result = list as ArrayList<MoviesData>
            )
        }
    }

    sealed class State {
        object ShowLoading : State()
        object HideLoading : State()
        data class PopularMovies(val result: ArrayList<MoviesData>) : State()
        data class NowPlayingMovies(val result: ArrayList<MoviesData>) :
            State()
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}
