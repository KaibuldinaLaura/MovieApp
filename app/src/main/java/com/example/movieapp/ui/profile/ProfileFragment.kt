package com.example.movieapp.ui.profile

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.movieapp.R
import com.example.movieapp.ui.MainActivity


class ProfileFragment: Fragment() {

    lateinit var rootView: View
    lateinit var myPrefs: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_profile, container, false)

        val tvName = rootView.findViewById<TextView>(R.id.tvName)
        val tvSurname = rootView.findViewById<TextView>(R.id.tvSurname)

        myPrefs = activity?.getSharedPreferences("prefID", Context.MODE_PRIVATE)!!
        tvName.text = "Name: " + myPrefs.getString("name", null)
        tvSurname.text = "Surname: " + myPrefs.getString("surname", null)

        return rootView
    }
}