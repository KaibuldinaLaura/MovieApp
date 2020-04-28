package com.example.movieapp.ui.login

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.movieapp.repository.UserRepository
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class LoginActivityViewModel(application: Application) : AndroidViewModel(application) {

    private val job = Job()
    private val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    private val uiScope: CoroutineScope = CoroutineScope(coroutineContext)

    private val _liveData = MutableLiveData<State>()
    val liveData: LiveData<State>
        get() = _liveData

    private var userRepository: UserRepository = UserRepository()
    private var requestToken: String? = null

    fun checkSession(sessionId: String) {
        uiScope.launch {
            _liveData.value = State.ShowLoading
            withContext(Dispatchers.IO) {
                try {
                    val response = userRepository.checkSession(sessionId)
                    if (response.isSuccessful) {
                        val result = response.body()
                        if (result != null && !result.username.isNullOrEmpty()) {
                            _liveData.postValue(
                                State.CheckSession(
                                2
                                )
                            )
                        } else {
                            Log.e("Error", "Cannot check session!")
                        }
                    } else {
                        Log.e("Error", "Cannot check session!")
                    }
                } catch (e: Exception) {
                    Log.e("Error", e.toString())
                }
            }
            _liveData.value = State.HideLoading
        }
    }

    fun createLogin(username: String, password: String) {
        uiScope.launch {
            _liveData.value = State.ShowLoading
            withContext(Dispatchers.IO) {
                try {
                    val response = userRepository.createToken()
                    if (response.isSuccessful) {
                        val result = response.body()
                        if (result != null) {
                            requestToken = result.getAsJsonPrimitive(
                                "request_token"
                            )?.asString
                            validateLogin(username, password)
                        } else {
                            Log.e("Error", "Cannot create Login")
                        }
                    } else {
                        Log.e("Error", "Cannot create Login")
                    }
                } catch (e: Exception) {
                    Log.e("Error", e.toString())
                }
            }
            _liveData.value = State.HideLoading
        }
    }

    private fun validateLogin(username: String, password: String) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val response =
                        requestToken?.let {
                            userRepository.validateWithLogin(
                                username, password,
                                it
                            )
                        }
                    if (response?.isSuccessful!!) {
                        val result = response.body()
                        if (result != null) {
                            if (result.getAsJsonPrimitive("success").asBoolean) {
                                createSession(requestToken)
                            } else {
                                Log.e("Error", "Cannot validate login!")
                            }
                        } else {
                            Log.e("Error", "Cannot validate login!!")
                        }
                    } else {
                        Log.e("Error", "Cannot validate login!!!")
                    }
                } catch (e: java.lang.Exception) {
                    Log.e("Error", e.toString())
                }
            }
            _liveData.value = State.HideLoading
        }
    }

    private fun createSession(requestToken: String?) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val response = requestToken?.let { userRepository.createSession(it) }
                    if (response!!.isSuccessful) {
                        val result = response.body()
                        if (result != null) {
                            _liveData.postValue(
                                result.getAsJsonPrimitive("session_id")?.asString?.let {
                                    State.LoginResult(
                                        it
                                    )
                                }
                            )
                        } else {
                            Log.e("Error", "Cannot create Session Id!")
                        }
                    } else {
                        Log.e("Error", "Cannot create Session Id!!")
                    }
                } catch (e: java.lang.Exception) {
                    Log.e("Error", e.toString())
                }
            }
            _liveData.value = State.HideLoading
        }
    }

    sealed class State {
        object ShowLoading : State()
        object HideLoading : State()
        data class LoginResult(val sessionId: String) : State()
        data class CheckSession(val value: Int) : State()
    }

}