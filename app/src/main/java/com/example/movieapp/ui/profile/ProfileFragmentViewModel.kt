package com.example.movieapp.ui.profile

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.movieapp.repository.MoviesRepository
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class ProfileFragmentViewModel(application: Application) : AndroidViewModel(application) {

    private val job = Job()
    private val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    private val uiScope: CoroutineScope = CoroutineScope(coroutineContext)

    private val _liveData = MutableLiveData<State>()
    val liveData: LiveData<State>
        get() = _liveData

    private var moviesRepository: MoviesRepository = MoviesRepository(application)

    fun getAccountDetails(sessionId: String) {
        uiScope.launch {
            _liveData.value = State.ShowLoading
            try {
                withContext(Dispatchers.IO) {
                    val response = moviesRepository.getAccountIdCoroutine(sessionId)
                    if (response.isSuccessful) {
                        val result = response.body()
                        if (result != null) {
                            _liveData.postValue(
                                State.AccountInfo(
                                    result
                                )
                            )
                        } else {
                            Log.e("Error", "Cannot get Account id")
                        }
                    } else {
                        Log.e("Error", "Cannot get Account id")
                    }
                }
            } catch (e: Exception) {
                Log.e("Error", e.toString())
            }
            _liveData.value = State.HideLoading
        }
    }

    sealed class State {
        object ShowLoading : State()
        object HideLoading : State()
        data class AccountInfo(val result: com.example.movieapp.model.data.AccountInfo) : State()
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}