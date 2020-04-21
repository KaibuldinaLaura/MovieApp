package com.example.movieapp.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.movieapp.model.data.MoviesData

<<<<<<< HEAD
@Database(entities = [MoviesData::class], version = 1)
=======
@Database(entities = [MoviesData::class], version = 2)
>>>>>>> master
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
<<<<<<< HEAD
                "second_database.db"
            )
                .fallbackToDestructiveMigration()
=======
                "movies_database.db"
            )
>>>>>>> master
                .build()
        }
            return INSTANCE!!
        }
    }
}