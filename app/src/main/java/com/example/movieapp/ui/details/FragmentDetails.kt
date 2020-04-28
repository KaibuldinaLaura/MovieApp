package com.example.movieapp.ui.details

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.model.data.MoviesData

class FragmentDetails : Fragment() {

    private lateinit var progressBar: ProgressBar
    private lateinit var movieImage: ImageView
    private lateinit var movieTitle: TextView
    private lateinit var movieRating: TextView
    private lateinit var movieDescription: TextView
    private lateinit var favouriteButton: CheckBox
    private lateinit var rootView: View
    private var favButtonState = false
    private var movieId: Int? = 0
    private lateinit var sessionId: String
    private val detailsFragmentAndFavouritesFragmentViewModel:
            DetailsFragmentAndFavouritesFragmentViewModel by viewModels()

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
        movieTitle = view.findViewById(R.id.titleTextView)
        movieDescription = view.findViewById(R.id.movieDescription)
        movieRating = view.findViewById(R.id.rating)
        favouriteButton = view.findViewById(R.id.imageButton)
        progressBar = view.findViewById(R.id.progressBar)
        movieId = arguments?.getInt("movie_id")

        favouriteButton.setOnClickListener {
            movieId?.let { id ->
                detailsFragmentAndFavouritesFragmentViewModel.
                    setFavouriteMovies(
                        !favButtonState,
                        sessionId,
                        id
                    )

            }
            favButtonState = if (favButtonState) {
                favouriteButton.setBackgroundResource(R.drawable.fav_icon)
                false
            } else {
                favouriteButton.setBackgroundResource(R.drawable.favicon)
                true
            }

        }
    }

    private fun setData() {
        movieId?.let { detailsFragmentAndFavouritesFragmentViewModel.getMovieById(it) }
        detailsFragmentAndFavouritesFragmentViewModel.getFavouriteMovies(sessionId, 1)

        detailsFragmentAndFavouritesFragmentViewModel.liveData.
            observe(viewLifecycleOwner, Observer { result ->
                when(result) {
                    is DetailsFragmentAndFavouritesFragmentViewModel.State.ShowLoading -> {
                        progressBar.visibility = View.VISIBLE
                    }
                    is DetailsFragmentAndFavouritesFragmentViewModel.State.HideLoading -> {
                        progressBar.visibility = View.GONE
                    }
                    is DetailsFragmentAndFavouritesFragmentViewModel.State.MovieById -> {
                        initView(result.result)
                    }
                    is DetailsFragmentAndFavouritesFragmentViewModel.State.FavouriteMovies -> {
                        checkFavList(result.result)
                    }
                }
            })
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

    private fun checkFavList(result: ArrayList<MoviesData>) {
        result.forEach {
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