package com.example.movieapp.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.movieapp.R
import org.w3c.dom.Text


class ProfileFragment: Fragment() {

    lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_profile, container, false)

        val tvName = rootView.findViewById<TextView>(R.id.tvName)
        val tvSurname = rootView.findViewById<TextView>(R.id.tvSurname)

        if (this.arguments != null) tvName.text = arguments!!.getString("name")

        return rootView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }
}