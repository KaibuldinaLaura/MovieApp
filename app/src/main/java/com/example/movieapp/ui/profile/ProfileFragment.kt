package com.example.movieapp.ui.profile

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.movieapp.R
import com.example.movieapp.model.data.AccountInfo
import com.example.movieapp.model.network.RetrofitService
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.util.*
import kotlin.coroutines.CoroutineContext


class ProfileFragment : Fragment() {

    private lateinit var profileName: TextView
    private lateinit var profileUsername: TextView
    private lateinit var progressBar: ProgressBar
    private var sessionId: String? = null
    private val profileFragmentViewModel: ProfileFragmentViewModel by viewModels()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.hide()
        val pref = context?.getSharedPreferences("prefSessionId", MODE_PRIVATE)
        sessionId = pref?.getString("session_id", "empty").toString()
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindView(view)
        setData()
    }

    private fun bindView(view: View) = with(view) {
        profileName = view.findViewById(R.id.profileNmae)
        profileUsername = view.findViewById(R.id.profileUsername)
        progressBar = view.findViewById(R.id.progressBar)
    }

    @SuppressLint("SetTextI18n")
    private fun setData() {
        sessionId?.let { profileFragmentViewModel.getAccountDetails(it) }
        profileFragmentViewModel.liveData.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is ProfileFragmentViewModel.State.ShowLoading -> {
                    progressBar.visibility = View.VISIBLE
                }
                is ProfileFragmentViewModel.State.HideLoading -> {
                    progressBar.visibility = View.GONE
                }
                is ProfileFragmentViewModel.State.AccountInfo -> {
                    profileName.text = "Name: " + result.result.name
                    profileUsername.text = "Username: " + result.result.username
                }
            }
        })
    }
}