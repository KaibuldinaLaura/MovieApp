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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var buttonReg: Button
    private lateinit var username: EditText
    private lateinit var password: EditText
    private var requestedToken: String? = null
    private var sessionId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.registration_page)
        bindView()
    }

    private fun bindView() {
        username = findViewById(R.id.loginUsername)
        password = findViewById(R.id.loginPassword)
        buttonReg = findViewById(R.id.buttonReg)

        buttonReg.setOnClickListener {
            if (username.text.toString().isNotEmpty()
                && password.text.toString().isNotEmpty()
            ) {
                createToken()
            } else Toast.makeText(
                applicationContext,
                "Please fill each field!", Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    private fun createToken() {
        RetrofitService.getMovieApi().createRequestToken().enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.e("Error", "Cannot create Token")
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null) {
                        requestedToken = result.
                        getAsJsonPrimitive("request_token")?.asString
                        validationWithLogin()
                    }
                }
            }
        })
    }

    private fun createSessionId() {
        val body = JsonObject().apply {
            addProperty("request_token", requestedToken)
        }
        RetrofitService.getMovieApi().createSession(body).enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.e("Error", "Cannot create Session Id")
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null) {
                        sessionId = result.getAsJsonPrimitive("session_id")?.asString
                        accessActivity()
                    }
                }
            }
        })
    }

    private fun validationWithLogin() {
        val body = JsonObject().apply {
            addProperty("username", username.text.toString())
            addProperty("password", password.text.toString())
            addProperty("request_token", requestedToken)
        }
        RetrofitService.getMovieApi().login(body).enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(
                    this@LoginActivity,
                    "Incorrect data", Toast.LENGTH_SHORT
                ).show()
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null) {
                        if (result.getAsJsonPrimitive("success")?.asBoolean!!) {
                            createSessionId()
                        }
                    }
                }
            }
        })
    }

    private fun accessActivity() {
        val myPrefs: SharedPreferences =
            getSharedPreferences("prefSessionId", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = myPrefs.edit()
        editor.putString("session_id", sessionId)
        editor.apply()

        val intent = Intent(baseContext, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
