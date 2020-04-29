package com.example.movieapp.model.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movieapp.model.data.CinemaData
@Dao
interface CinemaDao {
    @Query("SELECT * FROM cinema_table")
    fun getCinemas(): List<CinemaData>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(cinemaList: ArrayList<CinemaData>)

    @Query("DELETE FROM cinema_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM cinema_table WHERE id=:id")
    fun getCinema(id: Int): CinemaData
}