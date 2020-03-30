package com.example.movieapp.ui.favourites

import android.view.View

interface OnItemClickListner {
    abstract fun onItemClick(position: Int, view: View)
}