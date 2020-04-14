package com.example.movieapp.ui.profile

import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.movieapp.R
import com.example.movieapp.model.AccountInfo
import com.example.movieapp.retrofit.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProfileFragment: Fragment() {

    private lateinit var rootView: View
    private lateinit var pref: SharedPreferences
    private var sessionId: String? = null
    lateinit var tvName: TextView
    lateinit var tvUsername: TextView

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_profile, container, false)
        tvName = rootView.findViewById(R.id.tvName)
        tvUsername = rootView.findViewById(R.id.tvUsername)
        pref = this.activity?.getSharedPreferences("prefSessionId", MODE_PRIVATE)!!
        sessionId = pref.getString("session_id", "empty")
        getAccountDetails()
        return rootView
    }
    private fun getAccountDetails() {
        Log.d("start", "account")
        try {
            if (sessionId != null) {
                RetrofitService.getMovieApi()
                    .getAccountId(sessionId!!)
                    .enqueue(object : Callback<AccountInfo?> {
                        override fun onFailure(call: Call<AccountInfo?>, t: Throwable) {
                            Log.d("fav", "no")
                        }
                        @SuppressLint("SetTextI18n")
                        override fun onResponse(
                            call: Call<AccountInfo?>,
                            response: Response<AccountInfo?>
                        ) {
                            Log.d("start", response.body().toString())
                            tvName.text = "Name: " + response.body()?.name
                            tvUsername.text = "Username: " + response.body()?.username
                        }

                    })
            }
        } catch (e: Exception) {
            Log.d("mark",e.toString())
        }
    }
}