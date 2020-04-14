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
import com.example.movieapp.model.data.MovieResponse
import com.example.movieapp.model.data.MoviesData
import com.example.movieapp.model.network.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class FavouritesFragment: Fragment() {

    private lateinit var favouriteMoviesRecyclerView: RecyclerView
    private  var favouriteMoviesAdapter: FavouritesAdapter? = null
    private lateinit var sessionId: String
    private lateinit var navController: NavController

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

    private fun getFavouriteMovies() {
        favouriteMoviesAdapter?.clear()
        RetrofitService.getMovieApi().getFavoriteMovies(sessionId, page = 1)
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
                            favouriteMoviesAdapter
                                ?.addItems(result.movies as ArrayList<MoviesData>)
                        }
                    }
                }
            })
    }
}