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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(moviesData: List<MoviesData>?)

}