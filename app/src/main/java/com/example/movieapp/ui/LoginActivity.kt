package com.example.movieapp.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.movieapp.R
import com.example.movieapp.model.network.RetrofitService
import com.google.gson.JsonObject
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import android.widget.ProgressBar

class LoginActivity : AppCompatActivity() {

    private lateinit var buttonReg: Button
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var progressBar: ProgressBar
    private var requestedToken: String? = null
    private var sessionId: String? = null
    private val job = Job()

    private val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    private val uiScope: CoroutineScope = CoroutineScope(coroutineContext)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registration_page)
        bindView()
    }

    private fun bindView() {
        username = findViewById(R.id.loginUsername)
        password = findViewById(R.id.loginPassword)
        buttonReg = findViewById(R.id.buttonReg)
        progressBar = findViewById(R.id.loginProgressBar)
        buttonReg.setOnClickListener {
            if (username.text.toString().isNotEmpty()
                && password.text.toString().isNotEmpty()
            ) {
                buttonReg.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
                createToken()
            } else Toast.makeText(
                applicationContext,
                "Please fill each field!", Toast.LENGTH_SHORT
            ).show()
        }
        buttonReg.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }

    private fun createToken() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val response = RetrofitService.getMovieApi().createRequestToken()
                    Log.d("RES", response.body().toString())
                    if (response.isSuccessful) {
                        val result = response.body()
                        if (result != null) {
                            Log.d("Done", "Token Created")
                            requestedToken = result.getAsJsonPrimitive(
                                "request_token"
                            )?.asString
                            validationWithLogin()
                        } else {
                            Log.e("Error", "Cannot create token!")

                        }
                    } else {
                        Log.e("Error", "Cannot create token!!")

                    }
                } catch (e: Exception) {
                    Log.e("Error", "Cannot create token!!!")
                    buttonReg.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun createSessionId() {
        val body = JsonObject().apply {
            addProperty("request_token", requestedToken)
        }
        uiScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val response = RetrofitService.getMovieApi().createSession(body)
                    if (response.isSuccessful) {
                        val result = response.body()
                        if (result != null) {
                            Log.d("Done", "Token Created")
                            sessionId = result.getAsJsonPrimitive("session_id")?.asString
                            accessActivity(1)
                        } else {
                            Log.e("Error", "Cannot create session_id")
                        }
                    } else {
                        Log.e("Error", "Cannot create session_id")
                    }
                } catch (e: Exception) {
                    Log.e("Error", "Cannot create session_id")
                }
            }
        }
    }

    private fun validationWithLogin() {
        val body = JsonObject().apply {
            addProperty("username", username.text.toString())
            addProperty("password", password.text.toString())
            addProperty("request_token", requestedToken)
        }
        uiScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val response = RetrofitService.getMovieApi().login(body)
                    if (response.isSuccessful) {
                        val result = response.body()
                        if (result != null) {
                            Log.d("Done", "Token Created")
                        }
                        if (result?.getAsJsonPrimitive("success")?.asBoolean!!) {
                            createSessionId()
                        }
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "Incorrect data", Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(
                        this@LoginActivity,
                        "Incorrect data", Toast.LENGTH_SHORT
                    ).show()
                    buttonReg.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                }
            }
        }
    }


    private fun accessActivity(value: Int) {
        if (value == 1) {
            val myPrefs = getSharedPreferences("prefSessionId", Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = myPrefs.edit()
            editor.putString("session_id", sessionId)
            editor.apply()
        }

        val intent = Intent(baseContext, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
