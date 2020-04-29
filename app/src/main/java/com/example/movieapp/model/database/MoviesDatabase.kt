package com.example.movieapp.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.movieapp.model.data.CinemaData
import com.example.movieapp.model.data.MoviesData

@Database(entities = [MoviesData::class, CinemaData::class], version = 3)
abstract class MoviesDatabase: RoomDatabase() {
    abstract fun moviesDao(): MoviesDao
    abstract fun cinemaDao(): CinemaDao
    companion object {
        @Volatile
        private var INSTANCE: MoviesDatabase? = null

        fun getDatabase(
            context: Context
        ): MoviesDatabase? {
            if (INSTANCE == null){
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    MoviesDatabase::class.java,
                    "best_team.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return INSTANCE!!
        }
    }
}