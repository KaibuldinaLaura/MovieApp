package com.example.movieapp.repository

import com.example.movieapp.model.data.AccountInfo
import com.example.movieapp.model.network.RetrofitService
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import retrofit2.Response
import retrofit2.Retrofit
import kotlin.coroutines.CoroutineContext

class UserRepository : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private var retrofit = RetrofitService.getMovieApi()

    suspend fun createToken(): Response<JsonObject> {
        return retrofit.createRequestToken()
    }

    suspend fun validateWithLogin(username: String, password: String, requestedToken: String):
            Response<JsonObject> {
        val body = JsonObject().apply {
            addProperty("username", username.toString())
            addProperty("password", password.toString())
            addProperty("request_token", requestedToken)
        }
        return retrofit.login(body)
    }

    suspend fun createSession(requestedToken: String): Response<JsonObject> {
        val body = JsonObject().apply {
            addProperty("request_token", requestedToken)
        }
        return retrofit.createSession(body)
    }

    suspend fun checkSession(sessionId: String): Response<AccountInfo> {
        return retrofit.getAccountId(sessionId)
    }
}