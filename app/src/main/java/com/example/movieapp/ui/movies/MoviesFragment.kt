package com.example.movieapp.ui.movies

import android.os.Bundle
import android.util.Log
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
import com.example.movieapp.ui.movies.adapters.NowPlayingMoviesAdapter
import com.example.movieapp.ui.movies.adapters.PopularMoviesAdapter
import com.example.movieapp.utils.PaginationListener

open class MoviesFragment : Fragment() {

    private lateinit var popularMoviesRecyclerView: RecyclerView
    private lateinit var nowPlayingMoviesRecyclerView: RecyclerView
    private lateinit var nowPlayingMoviesProgressBar: ProgressBar
    private lateinit var popularMoviesProgressBar: ProgressBar
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var popularMoviesAdapter: PopularMoviesAdapter? = null
    private var nowPlayingMoviesAdapter: NowPlayingMoviesAdapter? = null
    private lateinit var navController: NavController
    private val moviesFragmentViewModel: MoviesFragmentViewModel by viewModels()

    private var popularMoviesCurrentPage = PaginationListener.PAGE_START
    private var popularMoviesIsLastPage = false
    private var popularMoviesIsLoading = false
    private var popularMoviesItemCount = 0

    private var nowPlayingMoviesCurrentPage = PaginationListener.PAGE_START
    private var nowPlayingMoviesIsLastPage = false
    private var nowPlayingMoviesIsLoading = false
    private var nowPlayingMoviesItemCount = 0

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
        setData()
    }

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
            popularMoviesAdapter?.clearAll()
            nowPlayingMoviesAdapter?.clearAll()
            moviesFragmentViewModel.getNowPlayingMovies()
            moviesFragmentViewModel.getPopularMovies()
        }
    }

    private fun setUpAdapter() {
        val popularMoviesLinearLayoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        popularMoviesRecyclerView.layoutManager = popularMoviesLinearLayoutManager

        popularMoviesRecyclerView.setHasFixedSize(true)
        popularMoviesRecyclerView.addOnScrollListener(object :
            PaginationListener(popularMoviesLinearLayoutManager) {
            override fun loadMoreItems() {
                popularMoviesIsLoading = true
                popularMoviesCurrentPage++
                moviesFragmentViewModel.getPopularMovies(page = popularMoviesCurrentPage)
            }

            override fun isLoading(): Boolean = popularMoviesIsLoading
            override fun isLastPage(): Boolean = popularMoviesIsLastPage
        })
        popularMoviesAdapter = PopularMoviesAdapter()
        popularMoviesRecyclerView.adapter = popularMoviesAdapter
        val nowPlayingMoviesLinearLayoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        nowPlayingMoviesRecyclerView.layoutManager = nowPlayingMoviesLinearLayoutManager
        nowPlayingMoviesRecyclerView.addOnScrollListener(object :
            PaginationListener(nowPlayingMoviesLinearLayoutManager) {
            override fun loadMoreItems() {
                nowPlayingMoviesIsLoading = true
                nowPlayingMoviesCurrentPage++
                moviesFragmentViewModel.getNowPlayingMovies(page = nowPlayingMoviesCurrentPage)
            }

            override fun isLastPage(): Boolean = nowPlayingMoviesIsLastPage
            override fun isLoading(): Boolean = nowPlayingMoviesIsLoading
        })
        nowPlayingMoviesAdapter = NowPlayingMoviesAdapter()
        nowPlayingMoviesRecyclerView.adapter = nowPlayingMoviesAdapter



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

    private fun setData() {
        popularMoviesAdapter?.clearAll()
        nowPlayingMoviesAdapter?.clearAll()

        moviesFragmentViewModel.liveData.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is MoviesFragmentViewModel.State.ShowLoading -> {
                    nowPlayingMoviesProgressBar.visibility = View.VISIBLE
                    popularMoviesProgressBar.visibility = View.VISIBLE
                }
                is MoviesFragmentViewModel.State.HideLoading -> {
                    nowPlayingMoviesProgressBar.visibility = View.GONE
                    popularMoviesProgressBar.visibility = View.GONE
                }
                is MoviesFragmentViewModel.State.PopularMovies -> {
                    popularMoviesItemCount = result.result.size
                    if (popularMoviesCurrentPage != PaginationListener.PAGE_START) {
                        popularMoviesAdapter?.removeLoading()
                    }
                    popularMoviesAdapter?.addItems(result.result)
                    if (popularMoviesCurrentPage < result.totalPages) {
                        popularMoviesAdapter?.addLoading()
                    } else {
                        popularMoviesIsLastPage = true
                    }
                    popularMoviesIsLoading = false
                }
                is MoviesFragmentViewModel.State.NowPlayingMovies -> {
                    nowPlayingMoviesItemCount = result.result.size
                    if (nowPlayingMoviesCurrentPage != PaginationListener.PAGE_START) {
                        nowPlayingMoviesAdapter?.removeLoading()
                    }
                    nowPlayingMoviesAdapter?.addItems(result.result)
                    if (nowPlayingMoviesCurrentPage < result.totalPages) {
                        nowPlayingMoviesAdapter?.addLoading()
                    } else {
                        nowPlayingMoviesIsLastPage = true
                    }
                    nowPlayingMoviesIsLoading = false
                }
            }
        })
    }
}