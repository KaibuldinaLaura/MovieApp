package com.example.movieapp.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.model.MoviesData
import com.example.movieapp.retrofit.RetrofitService
import com.example.movieapp.ui.favourites.FavMovie
import com.example.movieapp.ui.favourites.FavResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailsActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var textViewTitle: TextView
    private lateinit var textViewDesc: TextView
    private lateinit var likeBtn: ImageButton
    private lateinit var textViewRating: TextView
    var sessionId: String ?=null
    private var movieId:Int?=null
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        Log.e("qwe","123")

        val movieID = intent.getIntExtra("movieId", 1)

        textViewTitle = findViewById(R.id.text_view_title)
        textViewDesc = findViewById(R.id.text_view_description)
        imageView = findViewById(R.id.image_view)
        likeBtn = findViewById(R.id.imageButton)
        textViewRating = findViewById(R.id.rating)

        textViewDesc.text = "Showing information of "
        val pref = getSharedPreferences("prefID",Context.MODE_PRIVATE)
        sessionId = pref.getString("sessionID", "empty")

        getMovieById(movieID)

    }

    private fun getMovieById(movieId: Int) {
        RetrofitService.getMovieApi().getMovieById(movieId)
            .enqueue(object : Callback<MoviesData> {
                override fun onFailure(call: Call<MoviesData>, t: Throwable) {
                    Log.e("Error", "Error")
                }

                @SuppressLint("SetTextI18n")
                override fun onResponse(
                    call: Call<MoviesData>,
                    response: Response<MoviesData>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        Log.d("Check", responseBody?.title ?: "google")
                        if (responseBody != null) {
                            textViewTitle.text = responseBody.title
                            textViewDesc.text = responseBody.overview
                            textViewRating.text = "Your rating - " + responseBody.rating + "/10"
                            this@DetailsActivity.movieId = response.body()?.id
                            Glide.with(applicationContext)
                                .load("https://image.tmdb.org/t/p/w342${response.body()!!.posterPath}")
                                .into(this@DetailsActivity.imageView)
                            likeBtn.setOnClickListener {
                                markAsFav(FavMovie(true, movieId,"movie"), sessionId )
                                likeBtn.setImageResource(R.drawable.favicon)
                            }
                        }
                    }
                }
            })

    }

    fun markAsFav(info: FavMovie, sessionId: String?) {
        Log.d("start", "mfav$sessionId")
        try {

            RetrofitService.getMovieApi()
                .addFavList(info, sessionId)
                ?.enqueue(object : Callback<FavResponse?> {
                    override fun onFailure(call: Call<FavResponse?>, t: Throwable) {
                        Log.d("fav", "no")
                    }

                    override fun onResponse(
                        call: Call<FavResponse?>,
                        response: Response<FavResponse?>
                    ) {
                        Log.d("start", response.toString())
                        Log.d("start", response.body().toString())
                        if (response.body()?.status_code==12)
                            Toast.makeText(applicationContext,"Film added to Favourites",Toast.LENGTH_LONG).show()
                    }

                })
        } catch (e: Exception) {
            Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_SHORT).show()
            Log.d("mark",e.toString())
        }
    }
}