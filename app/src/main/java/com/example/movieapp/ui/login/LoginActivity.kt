package com.example.movieapp.ui.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.movieapp.R
import com.example.movieapp.ui.MainActivity


class LoginActivity : AppCompatActivity() {

    private lateinit var buttonReg: Button
    private lateinit var username: EditText
    private lateinit var password: EditText
    private var sessionId: String? = null
    private lateinit var progressBar: ProgressBar
    private val loginActivityViewModel: LoginActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registration_page)
        this.supportActionBar?.hide()
        val pref = this.getSharedPreferences("prefSessionId", Context.MODE_PRIVATE)
        sessionId = pref.getString("session_id", "null")
        bindView()
        setData()
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
                loginActivityViewModel.createLogin(
                    username.text.toString(),
                    password.text.toString()
                )
            } else Toast.makeText(
                applicationContext,
                "Please fill each field!", Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun setData() {
        sessionId?.let { loginActivityViewModel.checkSession(it) }

        loginActivityViewModel.liveData.observe(this, Observer { result ->
            when (result) {
                is LoginActivityViewModel.State.ShowLoading -> {
                    buttonReg.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                }
                is LoginActivityViewModel.State.HideLoading -> {
                    buttonReg.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                }
                is LoginActivityViewModel.State.LoginResult -> {
                    if (!result.sessionId.isNullOrEmpty()) {
                        accessActivity(1, result.sessionId)
                    }
                }
                is LoginActivityViewModel.State.CheckSession -> {
                    accessActivity(2)
                }
            }
        })
    }


    private fun accessActivity(value: Int, sessionId: String? = null) {
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
