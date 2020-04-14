package com.example.movieapp.ui.favourites

import com.google.gson.annotations.SerializedName

class FavMovie(
    favorite: Boolean,
    mediaId: Int?,
    mediaType: String
){
    @SerializedName("favorite")  var favorite: Boolean = false
    @SerializedName("media_id")  var mediaId: Int = 0
    @SerializedName("media_type")  var mediaType:String=""
    init {
        this.favorite=favorite
        this.mediaId= mediaId!!
        this.mediaType=mediaType
    }
}