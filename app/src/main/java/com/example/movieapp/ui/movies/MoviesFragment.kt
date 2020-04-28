package com.example.movieapp.ui.movies

<<<<<<< HEAD
=======
import android.app.Application
import android.content.Intent
>>>>>>> master
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
<<<<<<< HEAD
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
    private lateinit var nowPlayingMoviesProgressBar: ProgressBar
    private lateinit var popularMoviesProgressBar: ProgressBar
    private lateinit var nowPlayingMoviesRecyclerView: RecyclerView
    private var popularMoviesAdapter: PopularMoviesAdapter? = null
    private var nowPlayingMoviesAdapter: NowPlayingMoviesAdapter? = null
    private lateinit var navController: NavController
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
=======
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
>>>>>>> master

    override fun onCreate(savedInstanceState: Bundle?) {
        (activity as AppCompatActivity).supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        onCreateComponent()
    }

    private fun onCreateComponent() {
<<<<<<< HEAD
        popularMoviesAdapter = PopularMoviesAdapter()
=======
        moviesAdapter = MoviesAdapter()
>>>>>>> master
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

<<<<<<< HEAD
    private fun bindView(view: View) = with(view) {
        navController = Navigation.findNavController(view)
        popularMoviesRecyclerView = view.findViewById(R.id.popularMoviesRecyclerView)
        nowPlayingMoviesRecyclerView = view.findViewById(R.id.nowPlayingMoviesRecyclerView)
        nowPlayingMoviesProgressBar = view.findViewById(R.id.nowPlayingMoviesProgressBar)
        popularMoviesProgressBar = view.findViewById(R.id.popularMoviesProgressBar)
        swipeRefreshLayout = view.findViewById(R.id.moviesFragmentSFL)

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            popularMoviesProgressBar.visibility = View.VISIBLE
            nowPlayingMoviesProgressBar.visibility = View.VISIBLE
            popularMoviesAdapter?.clear()
            nowPlayingMoviesAdapter?.clear()
            getPopularMovies(
                onSuccess = ::onPopularMoviesFetched,
                onError = ::onError
            )
            getNowPlayingMovies(
                onSuccess = ::onNowPlayingMoviesFetched,
                onError = ::onError
            )
        }
    }

    private fun setUpAdapter() {
        popularMoviesRecyclerView.layoutManager = LinearLayoutManager(
=======

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
>>>>>>> master
            activity,
            LinearLayoutManager.HORIZONTAL,
            false
        )
<<<<<<< HEAD
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

=======



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
>>>>>>> master
    }

    private fun getNowPlayingMovies(
        page: Int = 1
    ) {
<<<<<<< HEAD
        RetrofitService.getMovieApi().getPopularMovies(page = page)
            .enqueue(object : Callback<MovieResponse> {
                override fun onResponse(
                    call: Call<MovieResponse>,
                    response: Response<MovieResponse>
                ) {
                    popularMoviesProgressBar.visibility = View.GONE
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            onSuccess.invoke(responseBody.movies)
                        } else {
                            onError.invoke()
=======
        uiScope.launch {
            val list = withContext(Dispatchers.IO) {
                try {
                    val response =
                        RetrofitService.getMovieApi().getNowPlayingMovieCoroutine(page = page)
                    if (response.isSuccessful) {
                        val result = response.body()
                        if (result != null) {
                            moviesDao?.insert(result.movies)
>>>>>>> master
                        }
                        result?.movies
                    } else {
                        moviesDao?.getAllMovies() ?: emptyList()
                    }
                } catch (e: Exception) {
                    moviesDao?.getAllMovies() ?: emptyList<MoviesData>()
                }
<<<<<<< HEAD

                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    popularMoviesProgressBar.visibility = View.GONE
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
                    nowPlayingMoviesProgressBar.visibility = View.GONE
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
                    nowPlayingMoviesProgressBar.visibility = View.GONE
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
=======
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
>>>>>>> master
    }
}