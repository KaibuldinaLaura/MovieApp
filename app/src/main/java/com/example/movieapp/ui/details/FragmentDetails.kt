package com.example.movieapp.ui.details

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.model.data.MovieResponse
import com.example.movieapp.model.data.MoviesData
import com.example.movieapp.model.network.RetrofitService
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentDetails: Fragment() {

    private lateinit var movieImage: ImageView
    private lateinit var movieTitle: TextView
    private lateinit var movieRating: TextView
    private lateinit var movieDescription: TextView
    private lateinit var favouriteButton: CheckBox
    private lateinit var rootView: View
    private var favList = listOf<MoviesData>()
    private var favButtonState = false
    private var movieId: Int? = 0
    private lateinit var sessionId: String


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_details, container, false)
        val myPref = context?.getSharedPreferences("prefSessionId", Context.MODE_PRIVATE)
        sessionId = myPref?.getString("session_id", "null").toString()
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindView(view)
        setData()
    }

    private fun bindView(view: View) = with(view) {
        movieImage = view.findViewById(R.id.movieImageView)
        movieTitle = view.findViewById(R.id.text_view_title)
        movieDescription = view.findViewById(R.id.movieDescription)
        movieRating = view.findViewById(R.id.rating)
        favouriteButton = view.findViewById(R.id.imageButton)
        movieId = arguments?.getInt("movie_id")

        favouriteButton.setOnClickListener{
            setFavouriteMovies(!favButtonState)
        }
    }

    private fun setData() {
        getMovieById()
        getFavouriteMovies()
    }

    private fun getMovieById() {
        movieId?.let { RetrofitService.getMovieApi().getMovieById(it)
            .enqueue(object : Callback<MoviesData> {
                override fun onFailure(call: Call<MoviesData>, t: Throwable) {
                    Log.e("Error", "Cannot get Movie")
                }

                override fun onResponse(call: Call<MoviesData>, response: Response<MoviesData>) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        if (result != null) {
                            initView(result)
                        }
                    }
                }
            }) }
    }

    private fun initView(moviesData: MoviesData) {
        Glide.with(rootView)
            .load("https://image.tmdb.org/t/p/w342${moviesData.posterPath}")
            .into(movieImage)
        movieDescription.text = moviesData.overview
        movieTitle.text = moviesData.title
        movieRating.text = "Your rating - " + moviesData.rating + "/10"
    }

    private fun getFavouriteMovies() {
        RetrofitService.getMovieApi().getFavoriteMovies(sessionId, 1)
            .enqueue(object : Callback<MovieResponse> {
            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                Log.e("Error", "Cannot get Favourite Movies")
            }

            override fun onResponse(
                call: Call<MovieResponse>,
                response: Response<MovieResponse>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null) {
                        favList = result.movies
                        checkFavList()
                    }
                }
            }
        })
    }
    private fun setFavouriteMovies(favourite: Boolean) {
        val body = JsonObject().apply {
            addProperty("media_type", "movie")
            addProperty("media_id", movieId)
            addProperty("favorite", favourite)
        }
        Log.d("1234", favourite.toString())
        RetrofitService.getMovieApi().setMovieMark(sessionId,body)
            .enqueue(object : Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.d("Error", "Cannot mark as favourite")
                }

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                   if (response.isSuccessful) {
                       getFavouriteMovies()
                   }
                }
            })
    }

    private fun checkFavList() {
        favList.forEach {
          if (it.id == movieId){
                favouriteButton.setBackgroundResource(R.drawable.favicon)
                favButtonState = true
                return
            }
        }
        favouriteButton.setBackgroundResource(R.drawable.fav_icon)
        favButtonState  = false
    }
}