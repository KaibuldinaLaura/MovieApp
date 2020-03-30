package com.example.movieapp.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.movieapp.R


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

        var myPrefs: SharedPreferences

        buttonReg.setOnClickListener(View.OnClickListener {
            if(name?.text.toString().isNotEmpty()
            && surname?.text.toString().isNotEmpty()
            && login?.text.toString().isNotEmpty()
            && password?.text.toString().isNotEmpty()){

                myPrefs = getSharedPreferences("prefID", Context.MODE_PRIVATE);
                val editor: SharedPreferences.Editor = myPrefs.edit()
                editor.putString("name", name?.text.toString())
                editor.putString("surname", surname?.text.toString())
                editor.apply()
                //editor.commit();

                val intent = Intent( baseContext, SecondActivity::class.java)
                startActivity(intent)
            }
            else Toast.makeText(applicationContext,"Please fill each field!" ,Toast.LENGTH_SHORT).show();
        })

    }

}
