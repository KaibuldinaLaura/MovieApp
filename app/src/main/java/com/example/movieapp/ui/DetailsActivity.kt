package com.example.movieapp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.movieapp.R
import com.example.movieapp.model.MoviesData
import com.example.movieapp.retrofit.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailsActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        Log.e("qwe","123")

        val movieId = intent.getIntExtra("movieId", 1)

        textView = findViewById(R.id.text_view)
        textView.text = "Showing information of "

        getMovieById(movieId)

    }

    private fun getMovieById(movieId: Int) {
        RetrofitService.getMovieApi().getMovieById(movieId)
            .enqueue(object : Callback<MoviesData> {
                override fun onFailure(call: Call<MoviesData>, t: Throwable) {
                    Log.e("Error", "Error")
                }

                override fun onResponse(
                    call: Call<MoviesData>,
                    response: Response<MoviesData>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        Log.d("Check", responseBody?.title ?: "google")
                        if (responseBody != null) {
                            textView.text = responseBody.overview
                        }
                    }
                }
            })

    }

}