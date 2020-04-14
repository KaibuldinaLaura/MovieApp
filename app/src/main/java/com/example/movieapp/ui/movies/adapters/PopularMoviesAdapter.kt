package com.example.movieapp.ui.movies.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.base.BaseRecyclerViewAdapter
import com.example.movieapp.data.model.MoviesData

class PopularMoviesAdapter: BaseRecyclerViewAdapter<MoviesData>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MoviesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val myHolder = holder as? MoviesViewHolder
        getItem(position)?.let { myHolder?.bind(movie = it) }
    }

    inner class MoviesViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView),
            View.OnClickListener {
        private val movieImageView = itemView.findViewById<ImageView>(R.id.item_movie_image)
        private val movieTitle = itemView.findViewById<TextView>(R.id.movie_title)
        private val movieReleaseDate = itemView.findViewById<TextView>(R.id.release_date)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind (movie: MoviesData) {
            Glide.with(itemView)
                .load("https://image.tmdb.org/t/p/w342${movie.posterPath}")
                .into(movieImageView)
            movieTitle.text = movie.title
            movieReleaseDate.text = movie.releaseDate
        }

        override fun onClick(v: View?) {
            if (v != null) {
                itemClickListener?.onItemClick(adapterPosition, v)
            }
        }
    }
}

