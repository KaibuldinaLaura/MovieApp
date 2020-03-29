package com.example.movieapp.ui.movies

import android.content.ClipData.newIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.R
import com.example.movieapp.model.MoviesData
import com.example.movieapp.retrofit.RetrofitService
import com.example.movieapp.ui.DetailsActivity

open class MoviesFragment: Fragment() {

    lateinit var recyclerView: RecyclerView
    private var moviesAdapter: MoviesAdapter? = null
    private lateinit var rootView: View


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
        rootView = inflater.inflate(R.layout.fragment_movies, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        }

    private fun initView(){
        setUpAdapter()
        inititializeRecyclerView()
    }

    private fun setUpAdapter(){
        moviesAdapter?.setOnItemClickListener(onItemClickListener = object : OnItemClickListner {
            override fun onItemClick(position: Int, view: View) {
                val intent = Intent(activity, DetailsActivity::class.java)
                intent.putExtra("movieId", moviesAdapter!!.getItemId(position))
                startActivity(intent)
            }
        })
    }

    private fun inititializeRecyclerView() {
        recyclerView = rootView.findViewById(R.id.moviesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        recyclerView.adapter = moviesAdapter

        RetrofitService.getPopularMovies(
            onSuccess = :: onPopularMoviesFetched,
            onError = :: onError
        )
    }



    private fun onPopularMoviesFetched(movies: List<MoviesData>) {
        moviesAdapter?.addItems(movies as ArrayList<MoviesData>)
    }

    private fun onError() {
      Log.e("Error", "Error")
    }


}