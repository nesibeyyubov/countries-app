package com.nesib.countriesapp.ui.details.map

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.nesib.countriesapp.base.BaseFragment
import com.nesib.countriesapp.base.ScreenParams
import com.nesib.countriesapp.databinding.FragmentMapBinding

class DetailsMapFragment :
    BaseFragment<FragmentMapBinding, DetailsMapState, DetailsMapViewModel, DetailsMapFragment.Params>() {

    override val viewModel: DetailsMapViewModel
        get() = ViewModelProvider(this)[DetailsMapViewModel::class.java]


    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?) = FragmentMapBinding.inflate(
        inflater, container, false
    )

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.mapView.onCreate(savedInstanceState)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun initViews() {


        Log.d("mytag", "initViews: ${params?.latlng}")
        params?.latlng?.run {
            binding.mapView.getMapAsync {
                Log.d("mytag", "map got: ")
                it.addMarker(
                    MarkerOptions()
                        .position(LatLng(this[0], this[1]))
                        .title("Your Country")
                )
                it.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(this[0], this[1]), 5f))

            }
        }

    }

    override fun render(state: DetailsMapState) {

    }

    data class Params(val latlng: List<Double>) : ScreenParams

}