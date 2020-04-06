package com.example.movieapp.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.movieapp.model.data.MoviesData

@Database(entities = [MoviesData::class], version = 2)
abstract class MoviesDatabase: RoomDatabase() {
    abstract fun moviesDao(): MoviesDao

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
                "movies_database.db"
            )
                .build()
        }
            return INSTANCE!!
        }
    }
}