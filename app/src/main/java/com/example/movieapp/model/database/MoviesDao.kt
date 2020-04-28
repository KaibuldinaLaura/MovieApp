package com.example.movieapp.model.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movieapp.model.data.MoviesData

@Dao
interface MoviesDao  {

    @Query("SELECT * FROM movies_table")
    fun getAllMovies(): List<MoviesData>

    @Query("SELECT * FROM  movies_table WHERE favourite = :favourite")
    fun getFavMovies(favourite: Int): List<MoviesData>

    @Query("SELECT * FROM  movies_table WHERE nowPlayingMovies = :nowPlayingMovie")
    fun getNowPlayingMovies(nowPlayingMovie: Int): List<MoviesData>

    @Query("SELECT * FROM  movies_table WHERE popularMovies = :popularMovie")
    fun getPopularMovies(popularMovie: Int): List<MoviesData>

    @Query("UPDATE movies_table SET favourite = :favourite WHERE id = :movieId")
    fun updateFavMovie (favourite: Int, movieId: Int)

    @Query("UPDATE movies_table SET popularMovies = :popularMovie WHERE id = :movieId")
    fun updatePopularMovie (popularMovie: Int, movieId: Int)

    @Query("UPDATE movies_table SET nowPlayingMovies = :nowPlayingMovie WHERE id = :movieId")
    fun updateNowPlayingMovies (nowPlayingMovie: Int, movieId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(moviesData: List<MoviesData>?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(moviesData: MoviesData)

    @Query("DELETE FROM movies_table WHERE id = :movieId")
    fun deleteItem(movieId: Int)

    @Query("SELECT * FROM movies_table WHERE id = :movieId")
    fun getMovieById(movieId: Int): MoviesData

}