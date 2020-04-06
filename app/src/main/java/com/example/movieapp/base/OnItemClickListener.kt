package com.example.movieapp.base

import android.view.View

interface OnItemClickListner {
    abstract fun onItemClick(position: Int, view: View)
}