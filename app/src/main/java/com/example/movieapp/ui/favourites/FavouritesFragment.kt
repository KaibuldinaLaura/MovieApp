package com.example.movieapp.ui.favourites

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.movieapp.R
import com.example.movieapp.base.OnItemClickListener
import com.example.movieapp.ui.details.DetailsFragmentAndFavouritesFragmentViewModel

open class FavouritesFragment : Fragment() {

    private lateinit var favouriteMoviesRecyclerView: RecyclerView
    private var favouriteMoviesAdapter: FavouritesAdapter? = null
    private lateinit var sessionId: String
    private lateinit var navController: NavController
    private lateinit var progressBar: ProgressBar
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private val favouriteMoviesFragmentAndFavouritesFragmentViewModel:
            DetailsFragmentAndFavouritesFragmentViewModel by viewModels()

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
        setUpAdapter()
        setData()
    }

    private fun bindView(view: View) = with(view) {
        progressBar = view.findViewById(R.id.progressBar)
        favouriteMoviesRecyclerView = view.findViewById(R.id.favouriteMoviesRecyclerView)
        navController = Navigation.findNavController(view)
        swipeRefreshLayout = findViewById(R.id.favouritesFragmentSFL)

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            progressBar.visibility = View.VISIBLE
            favouriteMoviesAdapter?.clear()
            favouriteMoviesFragmentAndFavouritesFragmentViewModel.getFavouriteMovies(sessionId)
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

    private fun setData() {
        favouriteMoviesFragmentAndFavouritesFragmentViewModel.getFavouriteMovies(sessionId, 1)

        favouriteMoviesFragmentAndFavouritesFragmentViewModel.liveData.observe(
            viewLifecycleOwner,
            Observer { result ->
                when (result) {
                    is DetailsFragmentAndFavouritesFragmentViewModel.State.ShowLoading -> {
                        progressBar.visibility = View.VISIBLE
                    }
                    is DetailsFragmentAndFavouritesFragmentViewModel.State.HideLoading -> {
                        progressBar.visibility = View.GONE
                    }
                    is DetailsFragmentAndFavouritesFragmentViewModel.State.FavouriteMovies -> {
                        favouriteMoviesAdapter?.addItems(result.result)
                    }
                }
            })
    }
}