package com.example.movieapp.ui.cinema

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.movieapp.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class CinemaMapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private val cinemaMapViewModel: CinemaMapViewModel by viewModels()
    private lateinit var googleMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cinema_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindView(view)
        setData()
    }

    private fun bindView(view: View) = with(view) {
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this@CinemaMapFragment)
    }

    private fun setData() {
        cinemaMapViewModel.getCinemaList()
        cinemaMapViewModel.liveData.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is CinemaMapViewModel.State.CinemaList -> {
                    result.result.map {
                        if (it.latitude != null && it.longitude != null) {
                            val currentLatLng = LatLng(it.latitude, it.longitude)
                            googleMap.addMarker(
                                it.id?.toFloat()?.let { id ->
                                    MarkerOptions()
                                        .position(currentLatLng)
                                        .title(it.name)
                                        .alpha(id)
                                }
                            )
                            googleMap.animateCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    currentLatLng,
                                    12f
                                )
                            )
                        }
                    }
                }
            }
        })
    }

    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.setOnMarkerClickListener(this)
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        return false
    }
}