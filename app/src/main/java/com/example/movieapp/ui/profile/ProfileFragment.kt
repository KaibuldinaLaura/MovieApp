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
import com.example.movieapp.model.data.AccountInfo
import com.example.movieapp.model.network.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProfileFragment : Fragment() {

    private lateinit var pref: SharedPreferences
    private lateinit var profileName: TextView
    private lateinit var profileUsername: TextView
    private var sessionId: String? = null

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        pref = this.activity?.getSharedPreferences("prefSessionId", MODE_PRIVATE)!!
        sessionId = pref.getString("session_id", "empty")
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindView(view)
        getAccountDetails()
    }

    private fun bindView(view: View) = with(view) {
        profileName = view.findViewById(R.id.profileNmae)
        profileUsername = view.findViewById(R.id.profileUsername)
    }

    private fun getAccountDetails() {
        Log.d("start", "account")
        try {
            if (sessionId != null) {
                RetrofitService.getMovieApi().getAccountId(sessionId!!)
                    .enqueue(object : Callback<AccountInfo?> {
                        override fun onFailure(call: Call<AccountInfo?>, t: Throwable) {
                            Log.e("error", "Cannot get account info:(")
                        }

                        @SuppressLint("SetTextI18n")
                        override fun onResponse(
                            call: Call<AccountInfo?>,
                            response: Response<AccountInfo?>
                        ) {
                            profileName.text = "Name: " + response.body()?.name
                            profileUsername.text = "Username: " + response.body()?.username
                        }

                    })
            }
        } catch (e: Exception) {
            Log.e("error", e.toString())
        }
    }
}