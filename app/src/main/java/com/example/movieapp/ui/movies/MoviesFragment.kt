package com.example.movieapp.ui.movies

import android.os.Bundle
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
import com.example.movieapp.ui.movies.adapters.PopularMoviesAdapter
import com.example.movieapp.ui.movies.adapters.NowPlayingMoviesAdapter
import kotlinx.coroutines.*
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

open class MoviesFragment : Fragment() {

    private lateinit var popularMoviesRecyclerView: RecyclerView
    private lateinit var nowPlayingMoviesRecyclerView: RecyclerView
    private lateinit var nowPlayingMoviesProgressBar: ProgressBar
    private lateinit var popularMoviesProgressBar: ProgressBar
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var popularMoviesAdapter: PopularMoviesAdapter? = null
    private var nowPlayingMoviesAdapter: NowPlayingMoviesAdapter? = null
    private lateinit var navController: NavController
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
        return inflater.inflate(R.layout.fragment_movies, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindView(view)
        setUpAdapter()
    }

    private fun bindView(view: View) = with(view) {
        navController = Navigation.findNavController(view)
        popularMoviesRecyclerView = view.findViewById(R.id.popularMoviesRecyclerView)
        nowPlayingMoviesRecyclerView = view.findViewById(R.id.nowPlayingMoviesRecyclerView)
        nowPlayingMoviesProgressBar = view.findViewById(R.id.nowPlayingMoviesProgressBar)
        popularMoviesProgressBar = view.findViewById(R.id.popularMoviesProgressBar)
        swipeRefreshLayout = view.findViewById(R.id.moviesFragmentSFL)

        moviesDao = context?.let {
            MoviesDatabase.getDatabase(context = it)
                ?.moviesDao()
        }
        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            popularMoviesProgressBar.visibility = View.VISIBLE
            nowPlayingMoviesProgressBar.visibility = View.VISIBLE
            popularMoviesAdapter?.clear()
            nowPlayingMoviesAdapter?.clear()
            getPopularMovies()
            getNowPlayingMovies()
        }
    }

    private fun setUpAdapter() {
        popularMoviesRecyclerView.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        popularMoviesAdapter = PopularMoviesAdapter()
        popularMoviesRecyclerView.adapter = popularMoviesAdapter

        nowPlayingMoviesRecyclerView.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        nowPlayingMoviesAdapter = NowPlayingMoviesAdapter()
        nowPlayingMoviesRecyclerView.adapter = nowPlayingMoviesAdapter

        getPopularMovies()
        getNowPlayingMovies()

        popularMoviesAdapter?.setOnItemClickListener(onItemClickListener = object :
            OnItemClickListener {
            override fun onItemClick(position: Int, view: View) {
                val bundle = Bundle()
                popularMoviesAdapter?.getItem(position)?.id?.let { bundle.putInt("movie_id", it) }
                navController.navigate(R.id.action_moviesFragment_to_fragmentDetails, bundle)
            }
        })
        nowPlayingMoviesAdapter?.setOnItemClickListener(onItemClickListener = object :
            OnItemClickListener {
            override fun onItemClick(position: Int, view: View) {
                val bundle = Bundle()
                nowPlayingMoviesAdapter?.getItem(position)?.id?.let {
                    bundle.putInt("movie_id", it)
                }
                navController.navigate(R.id.action_moviesFragment_to_fragmentDetails, bundle)
            }
        })
    }

    private fun getPopularMovies(
        page: Int = 1
    ) {
        uiScope.launch {
            val list = withContext(Dispatchers.IO) {
                try {
                    val response =
                        RetrofitService.getMovieApi().getPopularMovies(page = page)
                    if (response.isSuccessful) {
                        val result = response.body()
                        result?.movies?.forEach {
                            moviesDao?.insertItem(it)
                            moviesDao?.updatePopularMovie(1, it.id)
                        }
                        result?.movies
                    } else {
                        moviesDao?.getPopularMovies(1) ?: emptyList()
                    }
                } catch (e: Exception) {
                    moviesDao?.getPopularMovies(1) ?: emptyList()
                }
            }
            popularMoviesAdapter?.addItems(list as ArrayList<MoviesData>)
            popularMoviesProgressBar.visibility = View.GONE
        }
    }

    private fun getNowPlayingMovies(
        page: Int = 1
    ) {
        uiScope.launch {
            val list = withContext(Dispatchers.IO) {
                try {
                    val response =
                        RetrofitService.getMovieApi().getNowPlayingMovies(page = page)
                    if (response.isSuccessful) {
                        val result = response.body()
                        result?.movies?.forEach {
                            moviesDao?.insertItem(it)
                            moviesDao?.updateNowPlayingMovies(1, it.id)
                        }
                        result?.movies
                    } else {
                        popularMoviesAdapter?.clear()
                        moviesDao?.getNowPlayingMovies(1) ?: emptyList()
                    }
                } catch (e: Exception) {
                    popularMoviesAdapter?.clear()
                    moviesDao?.getNowPlayingMovies(1) ?: emptyList()
                }
            }
            nowPlayingMoviesAdapter?.addItems(list as ArrayList<MoviesData>)
            nowPlayingMoviesProgressBar.visibility = View.GONE
        }
    }
}