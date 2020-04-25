package com.example.movieapp.ui.favourites
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.R
import com.example.movieapp.base.OnItemClickListener
import com.example.movieapp.model.data.MoviesData
import com.example.movieapp.model.database.MoviesDao
import com.example.movieapp.model.database.MoviesDatabase
import com.example.movieapp.model.network.RetrofitService
import kotlinx.coroutines.*
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

open class FavouritesFragment: Fragment() {

    private lateinit var favouriteMoviesRecyclerView: RecyclerView
    private  var favouriteMoviesAdapter: FavouritesAdapter? = null
    private lateinit var sessionId: String
    private lateinit var navController: NavController
    private val job = Job()

    private val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    private val uiScope: CoroutineScope = CoroutineScope(coroutineContext)

    private var moviesDao: MoviesDao? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onCreateComponent()
    }

    private fun onCreateComponent() {
        favouriteMoviesAdapter = FavouritesAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        favouriteMoviesRecyclerView = view.findViewById(R.id.favouriteMoviesRecyclerView)
        navController = Navigation.findNavController(view)
        moviesDao = context?.let { MoviesDatabase.getDatabase(context = it)?.moviesDao() }
    }

    private fun setUpAdapter() {
        favouriteMoviesRecyclerView.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.VERTICAL,
            false
        )
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
                            if (moviesDao?.getMovieById(it.id) == null) {
                                moviesDao?.insertItem(it)
                                moviesDao?.updateFavMovie(favourite = 1, movieId = it.id)
                            } else {
                                moviesDao?.updateFavMovie(favourite = 1, movieId = it.id)
                                Log.d("Fav", moviesDao!!.getMovieById(it.id).toString())
                            }
                        }
                        result?.movies
                    } else {
                        moviesDao?.getFavMovies(favourite = 1) ?: emptyList()
                    }
                } catch (e: Exception) {
                    moviesDao?.getFavMovies(favourite = 1) ?: emptyList<MoviesData>()
                }
            }
            favouriteMoviesAdapter?.addItems(list as ArrayList<MoviesData>)
        }
    }
}