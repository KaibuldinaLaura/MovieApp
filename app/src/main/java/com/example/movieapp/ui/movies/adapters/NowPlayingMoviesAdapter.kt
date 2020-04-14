package com.example.movieapp.ui.movies.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.base.BaseRecyclerViewAdapter
import com.example.movieapp.model.data.MoviesData

class NowPlayingMoviesAdapter : BaseRecyclerViewAdapter<MoviesData>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return NowPlayingMoviesViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_now_playing_movies, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val myHolder = holder as NowPlayingMoviesViewHolder
        getItem(position)?.let { myHolder.bind(moviesData = it) }
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
                .load("https://image.tmdb.org/t/p/w500${moviesData.posterPath}")
                .into(nowPlayingMovieImage)
        }

        override fun onClick(v: View?) {
            if (v != null) {
                itemClickListener?.onItemClick(adapterPosition, v)
            }
        }

    }
}