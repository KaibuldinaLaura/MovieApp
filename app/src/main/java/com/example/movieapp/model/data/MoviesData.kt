package com.example.movieapp.model.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "movies_table")
data class MoviesData (
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id") val id: Int,
    @ColumnInfo(name = "title")
    @SerializedName("title") val title: String,
    @ColumnInfo(name = "overview")
    @SerializedName("overview") val overview: String,
    @ColumnInfo(name = "poster_path")
    @SerializedName("poster_path") val posterPath: String,
    @ColumnInfo(name = "backdrop_path")
    @SerializedName("backdrop_path") val backdropPath: String,
    @ColumnInfo(name = "vote_average")
    @SerializedName("vote_average") val rating: Float,
    @ColumnInfo(name = "release_date")
    @SerializedName("release_date") val releaseDate: String,
    @ColumnInfo(name = "favourite", defaultValue = "0")
    val favourite: Int,
    @ColumnInfo(name = "nowPlayingMovies", defaultValue = "0")
    val nowPlayingMoves: Int,
    @ColumnInfo(name = "popularMovies", defaultValue = "0")
    val popularMovies: Int
)