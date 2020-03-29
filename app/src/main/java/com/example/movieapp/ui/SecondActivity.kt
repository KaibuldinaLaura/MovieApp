package com.example.movieapp.ui

import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.movieapp.R
import com.example.movieapp.ui.favourites.FavouritesFragment
import com.example.movieapp.ui.movies.MoviesFragment
import com.example.movieapp.ui.profile.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class SecondActivity : AppCompatActivity(){

    private lateinit var toolbar: ActionBar

    private val  mOnNavigationItemSelected = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigationMovieView ->  {
                toolbar.title = "Movies"
                val moviesFragment: Fragment = MoviesFragment()
                openFragment(moviesFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigationFavouriteView -> {
                toolbar.title = "Favourites"
                val favouritesFragment: Fragment = FavouritesFragment()
                openFragment(favouritesFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigationProfileView -> {
                toolbar.title = "Profile"
                val profileFragment: Fragment = ProfileFragment()
                openFragment(profileFragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }


    override fun onBackPressed() {
        super.onBackPressed()
        return
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = supportActionBar!!

        val bottomNavigation: BottomNavigationView = findViewById(R.id.navView)
        bottomNavigation.setOnNavigationItemSelectedListener (mOnNavigationItemSelected)

        if (savedInstanceState == null) {
            val fragment = MoviesFragment()
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit()
        }
    }
}