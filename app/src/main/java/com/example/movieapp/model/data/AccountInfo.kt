package com.example.movieapp.model.data

import com.google.gson.annotations.SerializedName

data class AccountInfo (
    @SerializedName("id") val id: Int? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("include_adult") val adult: Boolean? = null,
    @SerializedName("username") val username: String? = null
)