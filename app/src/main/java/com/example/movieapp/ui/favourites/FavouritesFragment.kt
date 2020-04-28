package com.example.movieapp.ui.favourites

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.movieapp.R
import com.example.movieapp.base.OnItemClickListener
import com.example.movieapp.model.data.MoviesData
import com.example.movieapp.model.database.MoviesDao
import com.example.movieapp.model.database.MoviesDatabase
import com.example.movieapp.model.network.RetrofitService
import kotlinx.coroutines.*
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

open class FavouritesFragment : Fragment() {

    private lateinit var favouriteMoviesRecyclerView: RecyclerView
    private var favouriteMoviesAdapter: FavouritesAdapter? = null
    private lateinit var sessionId: String
    private lateinit var navController: NavController
    private lateinit var progressBar: ProgressBar
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

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
        val myPref = requireActivity()
            .getSharedPreferences("prefSessionId", Context.MODE_PRIVATE)
        sessionId = myPref.getString("session_id", "null").toString()
        return inflater.inflate(R.layout.fragment_favourites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindView(view)
        getFavouriteMovies()
        setUpAdapter()
    }

    private fun bindView(view: View) = with(view){
        progressBar = view.findViewById(R.id.progressBar)
        favouriteMoviesRecyclerView = view.findViewById(R.id.favouriteMoviesRecyclerView)
        navController = Navigation.findNavController(view)
        swipeRefreshLayout = findViewById(R.id.favouritesFragmentSFL)

        moviesDao = context?.let { MoviesDatabase.getDatabase(context = it)?.moviesDao() }
        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            progressBar.visibility = View.VISIBLE
            favouriteMoviesAdapter?.clear()
            getFavouriteMovies()
        }
    }

    private fun setUpAdapter() {
        favouriteMoviesRecyclerView.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.VERTICAL,
            false
        )
        favouriteMoviesAdapter = FavouritesAdapter()
        favouriteMoviesRecyclerView.adapter = favouriteMoviesAdapter

        favouriteMoviesAdapter?.setOnItemClickListener(onItemClickListener = object :
            OnItemClickListener {
            override fun onItemClick(position: Int, view: View) {
                val bundle = Bundle()
                favouriteMoviesAdapter?.getItem(position)?.id?.let {
                    bundle.putInt("movie_id", it)
                }
                navController.navigate(R.id.action_favouritesFragment_to_fragmentDetails, bundle)
            }
        })
    }

    private fun getFavouriteMovies(
        page: Int = 1
    ) {
        uiScope.launch {
            val list = withContext(Dispatchers.IO) {
                try {
                    val response =
                        RetrofitService.getMovieApi().getFavoriteMovies(sessionId, page)
                    if (response.isSuccessful) {
                        val result = response.body()
                        result?.movies?.forEach {
                                moviesDao?.insertItem(it)
                                moviesDao?.updateFavMovie(favourite = 1, movieId = it.id)
                                Log.d("Fav", moviesDao!!.getMovieById(it.id).toString())
                        }
                        result?.movies
                    } else {
                        moviesDao?.getFavMovies(favourite = 1) ?: emptyList()
                    }
                } catch (e: Exception) {
                    moviesDao?.getFavMovies(favourite = 1) ?: emptyList()
                }
            }
            favouriteMoviesAdapter?.addItems(list as ArrayList<MoviesData>)
            progressBar.visibility = View.GONE
        }
    }
}