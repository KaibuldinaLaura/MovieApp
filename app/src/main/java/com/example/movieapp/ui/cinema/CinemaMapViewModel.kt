package com.example.movieapp.ui.cinema

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.movieapp.model.data.CinemaData
import com.example.movieapp.repository.CinemaRepository
import com.example.movieapp.utils.CinemaArrayList
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class CinemaMapViewModel(application: Application) : AndroidViewModel(application) {

    private val job = Job()
    private val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    private val uiScope: CoroutineScope = CoroutineScope(coroutineContext)

    private val _liveData = MutableLiveData<State>()
    val liveData: LiveData<State>
        get() = _liveData

    private var cinemaRepository: CinemaRepository = CinemaRepository(application)

    init {
        setCinemaList()
    }

    fun setCinemaList() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                cinemaRepository.setCinemaList(CinemaArrayList.cinemaList)
            }
        }
    }

    fun getCinemaList() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                val list = cinemaRepository.getCinemaList()
                _liveData.postValue(
                    State.CinemaList(list as ArrayList<CinemaData>)
                )
            }
        }
    }

    fun getCinemaById(cinemaId: Int) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                val cinema = cinemaRepository.getCinemaById(cinemaId)
                _liveData.postValue(
                    State.CinemaById(cinema)
                )
            }
        }

    }

    sealed class State {
        object ShowLoading : State()
        object HideLoading : State()
        data class CinemaList(val result: ArrayList<CinemaData>) : State()
        data class CinemaById(val result: CinemaData) : State()
    }

}
