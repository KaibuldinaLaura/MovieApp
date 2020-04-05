package com.example.movieapp.ui.favourites

import com.google.gson.annotations.SerializedName

class SessionId(token: String?){
    @SerializedName("request_token")  var request_token:String=""
    init {
        request_token= token!!
    }
}