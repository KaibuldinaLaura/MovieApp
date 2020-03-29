package com.example.movieapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.movieapp.R
import com.example.movieapp.ui.profile.ProfileFragment


class MainActivity : AppCompatActivity() {

    private lateinit var buttonReg: Button

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.registration_page)

        val name: EditText? = findViewById(R.id.editText1)
        val surname: EditText? = findViewById(R.id.editText2)
        val login: EditText? = findViewById(R.id.editText3)
        val password: EditText? = findViewById(R.id.editText4)
        buttonReg = findViewById(R.id.buttonReg)

        buttonReg.setOnClickListener(View.OnClickListener {
            if(name?.text.toString().isNotEmpty()
            && surname?.text.toString().isNotEmpty()
            && login?.text.toString().isNotEmpty()
            && password?.text.toString().isNotEmpty()){

                val bundle = Bundle()
                bundle.putString("name", name?.text.toString())
                //bundle.putString("surname", surname?.text.toString())
                val fragment = ProfileFragment()
                fragment.arguments = bundle
                val intent = Intent( baseContext, SecondActivity::class.java)
                startActivity(intent)
            }
            else Toast.makeText(applicationContext,"Please fill each field!" ,Toast.LENGTH_SHORT).show();
        })

    }

}
