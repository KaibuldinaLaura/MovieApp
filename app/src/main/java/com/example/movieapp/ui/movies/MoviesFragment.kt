package com.example.movieapp.ui.movies

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.R
import com.example.movieapp.base.OnItemClickListner
import com.example.movieapp.model.data.MoviesData
import com.example.movieapp.model.database.MoviesDao
import com.example.movieapp.model.database.MoviesDatabase
import com.example.movieapp.model.network.RetrofitService
import com.example.movieapp.ui.DetailsActivity
import com.example.movieapp.ui.movies.adapters.MoviesAdapter
import com.example.movieapp.ui.movies.adapters.NowPlayingMoviesAdapter
import kotlinx.coroutines.*
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

open class MoviesFragment : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var nowPlayingMoviesRecyclerView: RecyclerView
    private var moviesAdapter: MoviesAdapter? = null
    private var nowPlayingMoviesAdapter: NowPlayingMoviesAdapter? = null
    private lateinit var rootView: View
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
        moviesAdapter = MoviesAdapter()
        nowPlayingMoviesAdapter = NowPlayingMoviesAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_movies, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

    }

    private fun initView() {
        setUpAdapter()
        inititializeRecyclerView()
    }


    private fun setUpAdapter() {
        moviesAdapter?.setOnItemClickListener(onItemClickListener = object :
            OnItemClickListner {
            override fun onItemClick(position: Int, view: View) {
                val intent = Intent(activity, DetailsActivity::class.java)
                intent.putExtra("movieId", moviesAdapter!!.getItem(position)?.id)
                startActivity(intent)
            }
        })

        nowPlayingMoviesAdapter?.setOnItemClickListener(onItemClickListener = object :
            OnItemClickListner {
            override fun onItemClick(position: Int, view: View) {
                val intent = Intent(activity, DetailsActivity::class.java)
                intent.putExtra("movieId", nowPlayingMoviesAdapter!!.getItem(position)?.id)
                startActivity(intent)
            }
        })
    }

    private fun inititializeRecyclerView() {

        moviesDao = context?.let { MoviesDatabase.getDatabase(context = it)?.moviesDao() }

        recyclerView = rootView.findViewById(R.id.moviesRecyclerView1)
        recyclerView.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.HORIZONTAL,
            false
        )



        nowPlayingMoviesRecyclerView = rootView.findViewById(R.id.popularMoviesRecyclerView)
        nowPlayingMoviesRecyclerView.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        nowPlayingMoviesRecyclerView.adapter = nowPlayingMoviesAdapter
        recyclerView.adapter = moviesAdapter

        getPopularMovies()

        getNowPlayingMovies()
    }

    private fun getNowPlayingMovies(
        page: Int = 1
    ) {
        uiScope.launch {
            val list = withContext(Dispatchers.IO) {
                try {
                    val response =
                        RetrofitService.getMovieApi().getNowPlayingMovieCoroutine(page = page)
                    if (response.isSuccessful) {
                        val result = response.body()
                        if (result != null) {
                            moviesDao?.insert(result.movies)
                        }
                        result?.movies
                    } else {
                        moviesDao?.getAllMovies() ?: emptyList()
                    }
                } catch (e: Exception) {
                    moviesDao?.getAllMovies() ?: emptyList<MoviesData>()
                }
            }
            nowPlayingMoviesAdapter?.addItems(list as ArrayList<MoviesData>)
        }
    }

    private fun getPopularMovies(
        page: Int = 1
    ) {
        uiScope.launch {
            val list = withContext(Dispatchers.IO) {
                try {
                    val response =
                        RetrofitService.getMovieApi().getPopularMoviesCoroutine(page = page)
                    if (response.isSuccessful) {
                        val result = response.body()
                        if (result != null) {
                            moviesDao?.insert(result.movies)
                        }
                        result?.movies
                    } else {
                        moviesDao?.getAllMovies() ?: emptyList()
                    }
                } catch (e: Exception) {
                    moviesDao?.getAllMovies() ?: emptyList<MoviesData>()
                }
            }
            moviesAdapter?.addItems(list as ArrayList<MoviesData>)
        }
    }


    private fun onError() {
        Log.e("Error", "Error")
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}