package com.example.movieapp.ui.favourites
import android.content.Context
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
import com.example.movieapp.model.MovieResponse
import com.example.movieapp.model.MoviesData
import com.example.movieapp.retrofit.RetrofitService
import com.example.movieapp.ui.DetailsActivity
import com.example.movieapp.ui.movies.MoviesAdapter
import com.example.movieapp.ui.movies.OnItemClickListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class FavouritesFragment: Fragment() {

    private lateinit var recyclerView: RecyclerView
    private var moviesAdapter: MoviesAdapter? = null
    private lateinit var rootView: View
    private var sessionId: String ?=null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onCreateComponent()
    }

    private fun onCreateComponent() {
        moviesAdapter = MoviesAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val pref =
            activity!!.getSharedPreferences("prefID", Context.MODE_PRIVATE)
        sessionId = pref.getString("sessionID", "empty")
        rootView = inflater.inflate(R.layout.fragment_favourites
            , container, false)
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

    private fun setUpAdapter(){
        moviesAdapter?.setOnItemClickListener(onItemClickListener = object : OnItemClickListener {
            override fun onItemClick(position: Int, view: View) {
                val intent = Intent(activity, DetailsActivity::class.java)
                intent.putExtra("movieId", moviesAdapter!!.getItem(position)?.id)
                startActivity(intent)
            }
        })
    }

    private fun inititializeRecyclerView() {
        recyclerView = rootView.findViewById(R.id.moviesRecyclerView1)
        recyclerView.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.VERTICAL,
            false
        )
        recyclerView.adapter = moviesAdapter

        getPopularMovies(
            onSuccess = :: onPopularMoviesFetched,
            onError =  :: onError
        )
    }

    private fun getPopularMovies(
        onSuccess: (movies: List<MoviesData>) -> Unit,
        onError: () -> Unit
    ) {
        RetrofitService.getMovieApi().getFavList(sessionId)
            ?.enqueue(object : Callback<MovieResponse?> {
                override fun onResponse(
                    call: Call<MovieResponse?>,
                    response: Response<MovieResponse?>
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

                override fun onFailure(call: Call<MovieResponse?>, t: Throwable) {
                    onError.invoke()
                }
            })
    }


    private fun onPopularMoviesFetched(movies: List<MoviesData>) {
        moviesAdapter?.addItems(movies as ArrayList<MoviesData>)
    }
    private fun onError() {
        Log.e("Error", "Error")
    }
}