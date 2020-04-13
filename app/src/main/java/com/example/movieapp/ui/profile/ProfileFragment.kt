package com.example.movieapp.ui.profile

import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.movieapp.R


class ProfileFragment: Fragment() {

    private lateinit var rootView: View
    private lateinit var myPrefs: SharedPreferences

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_profile, container, false)

        val tvName = rootView.findViewById<TextView>(R.id.tvName)
        val tvSurname = rootView.findViewById<TextView>(R.id.tvSurname)
        val tvUsername = rootView.findViewById<TextView>(R.id.tvUsername)

        myPrefs = activity?.getSharedPreferences("prefID", MODE_PRIVATE)!!
        tvName.text = """Name: ${myPrefs.getString("name", null)}"""
        tvSurname.text = """Surname: ${myPrefs.getString("surname", null)}"""
        tvUsername.text = """Username: ${myPrefs.getString("username", null)}"""

        return rootView
    }
}