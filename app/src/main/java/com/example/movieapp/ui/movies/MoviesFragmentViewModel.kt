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

    init {
        getPopularMovies()
        getNowPlayingMovies()
    }

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
                        val totalPages = result?.pages
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
                        Pair(result?.movies, totalPages)
                    } else {
                        Pair(
                            moviesRepository.getPopularMovies(1) ?: emptyList(), 1
                        )
                    }
                } catch (e: Exception) {
                    Pair(
                        moviesRepository.getPopularMovies(1) ?: emptyList(), 1
                    )
                }
            }
            _liveData.value = State.HideLoading
            _liveData.value = list.second?.let {
                State.PopularMovies(
                    totalPages = it,
                    result = list.first as ArrayList<MoviesData>
                )
            }
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
                        val totalPages = result?.pages
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
                        Pair(result?.movies, totalPages)
                    } else {
                        Pair(
                            moviesRepository.getNowPlayingMovies(1) ?: emptyList(), 1
                        )
                    }
                } catch (e: Exception) {
                    Pair(
                        moviesRepository.getNowPlayingMovies(1) ?: emptyList(), 1
                    )
                }
            }
            _liveData.value = State.HideLoading
            _liveData.value = list.second?.let {
                State.NowPlayingMovies(
                    totalPages = it,
                    result = list.first as ArrayList<MoviesData>
                )
            }
        }
    }

    sealed class State {
        object ShowLoading : State()
        object HideLoading : State()
        data class PopularMovies(
            val totalPages: Int,
            val result: ArrayList<MoviesData>
        ) : State()

        data class NowPlayingMovies(
            val totalPages: Int,
            val result: ArrayList<MoviesData>
        ) : State()
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}
