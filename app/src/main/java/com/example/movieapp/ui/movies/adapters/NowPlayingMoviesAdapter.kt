package com.example.movieapp.ui.movies.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.base.BaseRecyclerViewAdapter
import com.example.movieapp.base.OnItemClickListener
import com.example.movieapp.model.data.MoviesData

class NowPlayingMoviesAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private val VIEW_TYPE_LOADING = 0
    private val VIEW_TYPE_NORMAL = 1
    var itemClickListener: OnItemClickListener? = null
    private var isLoaderVisible = false

    private val movieList = ArrayList<MoviesData>()

    fun clearAll() {
        movieList.clear()
        notifyDataSetChanged()
    }

    fun removeLoading() {
        isLoaderVisible = false
        val position = movieList.size - 1
        if (movieList.isNotEmpty()) {
            val item = getItem(position)
            if (item != null) {
                movieList.removeAt(position)
                notifyItemRemoved(position)
            }
        }
    }

    fun getItem(position: Int): MoviesData? {
        return movieList[position]
    }

    fun addItems(list: List<MoviesData>) {
        movieList.addAll(list)
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    fun addLoading() {
        isLoaderVisible = true
        movieList.add(MoviesData(id = -1, favourite = 0, popularMovies = 0, nowPlayingMoves = 1))
        notifyItemInserted(movieList.size - 1)
    }

    override fun getItemCount(): Int = movieList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when(viewType) {
            VIEW_TYPE_NORMAL -> NowPlayingMoviesViewHolder (
                inflater.inflate(R.layout.item_now_playing_movies, parent, false)
            )
            VIEW_TYPE_LOADING -> ProgressViewHolder (
                inflater.inflate(R.layout.item_now_playing_movie_loading, parent, false)
            )
            else -> throw Throwable("invalid view")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(isLoaderVisible) {
            if (position == movieList.size - 1) {
                VIEW_TYPE_LOADING
            } else {
                VIEW_TYPE_NORMAL
            }
        } else {
            VIEW_TYPE_NORMAL
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val myHolder = holder as? NowPlayingMoviesViewHolder
        getItem(position)?.let { myHolder?.bind(moviesData = it) }
    }

    inner class NowPlayingMoviesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        private val nowPlayingMovieImage =
            itemView.findViewById<ImageView>(R.id.nowPlayingMovieImage)

        init {
            nowPlayingMovieImage.setOnClickListener(this)
        }

        fun bind(moviesData: MoviesData) {
            Glide.with(itemView)
                .load("https://image.tmdb.org/t/p/w342${moviesData.posterPath}")
                .into(nowPlayingMovieImage)
        }

        override fun onClick(v: View?) {
            if (v != null) {
                itemClickListener?.onItemClick(adapterPosition, v)
            }
        }
    }
    inner class ProgressViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun clear() { }
    }
}