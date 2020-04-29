package com.example.movieapp.repository

import android.app.Application
import com.example.movieapp.model.data.CinemaData
import com.example.movieapp.model.database.CinemaDao
import com.example.movieapp.model.database.MoviesDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class CinemaRepository(application: Application): CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private lateinit var cinemaDao: CinemaDao
    init {
        val db = MoviesDatabase.getDatabase(application)
        if (db != null) {
            cinemaDao = db.cinemaDao()
        }
    }

    fun getCinemaList(): List<CinemaData> {
        return cinemaDao.getCinemas()
    }

    fun getCinemaById(cinemaId: Int): CinemaData {
        return cinemaDao.getCinema(cinemaId)
    }

    fun setCinemaList(cinemaList: ArrayList<CinemaData>) {
        launch {
            setCinemaListBG(cinemaList)
        }
    }
    suspend fun setCinemaListBG(cinemaList: ArrayList<CinemaData>) {
        withContext(Dispatchers.IO) {
            cinemaDao.insert(cinemaList)
        }
    }
}