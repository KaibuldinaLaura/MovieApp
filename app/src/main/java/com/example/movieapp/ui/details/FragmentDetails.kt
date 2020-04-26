package com.example.movieapp.ui.details

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.model.data.MoviesData
import com.example.movieapp.model.database.MoviesDao
import com.example.movieapp.model.database.MoviesDatabase
import com.example.movieapp.model.network.RetrofitService
import com.google.gson.JsonObject
import kotlinx.coroutines.*
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class FragmentDetails : Fragment() {

    private lateinit var progressBar: ProgressBar
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

    private val job = Job()
    private val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    private val uiScope: CoroutineScope = CoroutineScope(coroutineContext)

    private var moviesDao: MoviesDao? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.hide()
        rootView =  inflater.inflate(R.layout.fragment_details, container, false)
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
        progressBar = view.findViewById(R.id.progressBar)
        movieId = arguments?.getInt("movie_id")
        moviesDao = context?.let {
            MoviesDatabase.getDatabase(context = it)
                ?.moviesDao()
        }

        favouriteButton.setOnClickListener {
            setFavouriteMovies(!favButtonState)
        }
    }

    private fun setData() {
        getMovieById()
        getFavouriteMovies()
    }

    private fun getMovieById() {
        uiScope.launch {
            val list = withContext(Dispatchers.IO) {
                try {
                    val response =
                        movieId?.let { RetrofitService.getMovieApi().getMovieById(it) }
                    if (response?.isSuccessful!!) {
                        val result = response.body()
                        if (result != null) {
                            Log.d("Done", "Successfully got Movie by Id")
                        }
                        result
                    } else {
                        movieId?.let { moviesDao?.getMovieById(it) }
                    }
                } catch (e: Exception) {
                    movieId?.let { moviesDao?.getMovieById(it) }
                }
            }
            initView(list as MoviesData)
        }
    }

    private fun initView(moviesData: MoviesData) {
        progressBar.visibility = View.GONE
        Glide.with(rootView)
            .load("https://image.tmdb.org/t/p/w342${moviesData.posterPath}")
            .into(movieImage)
        movieDescription.text = moviesData.overview
        movieTitle.text = moviesData.title
        movieRating.text = "Your rating - " + moviesData.rating + "/10"
    }

    private fun getFavouriteMovies() {
        uiScope.launch {
            val list = withContext(Dispatchers.IO) {
                try {
                    val response =
                        RetrofitService.getMovieApi().getFavoriteMovies(sessionId, 1)
                    if (response.isSuccessful) {
                        val result = response.body()
                        result?.movies?.forEach {
                            moviesDao?.updateFavMovie(favourite = 1, movieId = it.id)
                        }
                        result?.movies
                    } else {
                        moviesDao?.getFavMovies(1) ?: emptyList()
                    }
                } catch (e: Exception) {
                    moviesDao?.getFavMovies(1) ?: emptyList<MoviesData>()
                }
            }
            favList = list as ArrayList<MoviesData>
            checkFavList()
        }
    }

    private fun setFavouriteMovies(favourite: Boolean) {
        val body = JsonObject().apply {
            addProperty("media_type", "movie")
            addProperty("media_id", movieId)
            addProperty("favorite", favourite)
        }
        uiScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val response =
                        RetrofitService.getMovieApi().setMovieMark(sessionId, body)
                    if (response.isSuccessful) {
                        val fav: Int = if (favourite) {
                            1
                        } else {
                            0
                        }
                        movieId?.let { moviesDao?.updateFavMovie(fav, movieId = it) }
                        getFavouriteMovies()
                    } else {
                        Log.e("Error", "Cannot mark as favourite")
                    }
                } catch (e: Exception) {
                    Log.e("Error", "Cannot mark as favourite")
                }
            }
        }
    }

    private fun checkFavList() {
        favList.forEach {
            if (it.id == movieId) {
                favouriteButton.setBackgroundResource(R.drawable.favicon)
                favButtonState = true
                return
            }
        }
        favouriteButton.setBackgroundResource(R.drawable.fav_icon)
        favButtonState = false
    }
}