package com.example.movieapp.ui.movies

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
import com.example.movieapp.model.data.MovieResponse
import com.example.movieapp.model.data.MoviesData
import com.example.movieapp.model.network.RetrofitService
import com.example.movieapp.ui.movies.adapters.PopularMoviesAdapter
import com.example.movieapp.ui.movies.adapters.NowPlayingMoviesAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class MoviesFragment : Fragment() {

    private lateinit var popularMoviesRecyclerView: RecyclerView
    private lateinit var nowPlayingMoviesRecyclerView: RecyclerView
    private var popularMoviesAdapter: PopularMoviesAdapter? = null
    private var nowPlayingMoviesAdapter: NowPlayingMoviesAdapter? = null
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onCreateComponent()
    }

    private fun onCreateComponent() {
        popularMoviesAdapter = PopularMoviesAdapter()
        nowPlayingMoviesAdapter = NowPlayingMoviesAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
    }

    private fun setUpAdapter() {
        popularMoviesRecyclerView.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        popularMoviesRecyclerView.adapter = popularMoviesAdapter
        nowPlayingMoviesRecyclerView.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        nowPlayingMoviesRecyclerView.adapter = nowPlayingMoviesAdapter

        getPopularMovies(
            onSuccess = ::onPopularMoviesFetched,
            onError = ::onError
        )
        getNowPlayingMovies(
            onSuccess = ::onNowPlayingMoviesFetched,
            onError = ::onError
        )

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
        page: Int = 1,
        onSuccess: (movies: List<MoviesData>) -> Unit,
        onError: () -> Unit
    ) {
        RetrofitService.getMovieApi().getPopularMovies(page = page)
            .enqueue(object : Callback<MovieResponse> {
                override fun onResponse(
                    call: Call<MovieResponse>,
                    response: Response<MovieResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            onSuccess.invoke(responseBody.movies)
                        } else {
                            onError.invoke()
                        }
                    } else {
                        onError.invoke()
                    }
                }

                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    onError.invoke()
                }
            })
    }

    private fun getNowPlayingMovies(
        page: Int = 1,
        onSuccess: (movies: List<MoviesData>) -> Unit,
        onError: () -> Unit
    ) {
        RetrofitService.getMovieApi().getNowPlayingMovies(page = page)
            .enqueue(object : Callback<MovieResponse> {
                override fun onResponse(
                    call: Call<MovieResponse>,
                    response: Response<MovieResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            onSuccess.invoke(responseBody.movies)
                        } else {
                            onError.invoke()
                        }
                    } else {
                        onError.invoke()
                    }
                }

                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    onError.invoke()
                }
            })
    }

    private fun onPopularMoviesFetched(movies: List<MoviesData>) {
        popularMoviesAdapter?.clear()
        popularMoviesAdapter?.addItems(movies as ArrayList<MoviesData>)
    }

    private fun onNowPlayingMoviesFetched(movies: List<MoviesData>) {
        nowPlayingMoviesAdapter?.clear()
        nowPlayingMoviesAdapter?.addItems(movies as ArrayList<MoviesData>)
    }

    private fun onError() {
        Log.e("Error", "Error")
    }
}