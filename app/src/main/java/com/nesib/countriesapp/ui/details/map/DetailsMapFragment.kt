package com.nesib.countriesapp.ui.details.map

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.nesib.countriesapp.R
import com.nesib.countriesapp.base.BaseFragment
import com.nesib.countriesapp.base.ScreenParams
import com.nesib.countriesapp.databinding.FragmentMapBinding


class DetailsMapFragment :
    BaseFragment<FragmentMapBinding, DetailsMapState, DetailsMapViewModel, DetailsMapFragment.Params>() {

    private val mapTypeButtons by lazy {
        listOf(
            MapTypeButton(binding.normalBtn, MapType.Normal),
            MapTypeButton(binding.terrainBtn, MapType.Terrain),
            MapTypeButton(binding.hybridBtn, MapType.Hybrid),
            MapTypeButton(binding.satelliteBtn, MapType.Satellite),
        )
    }

    private var googleMap: GoogleMap? = null

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

    override fun initViews() = with(binding) {
        params?.latlng?.run {
            binding.mapView.getMapAsync {
                this@DetailsMapFragment.googleMap = it
                it.addMarker(
                    MarkerOptions()
                        .position(LatLng(this[0], this[1]))
                        .title(params?.countryName)
                )
                it.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(this[0], this[1]), 5f))
            }
        }

        mapTypeButtons.forEach { mapTypeButton ->
            mapTypeButton.button.setOnClickListener {
                viewModel.mapTypeClicked(mapTypeButton.type)
            }
        }

        backButton.setOnClickListener { navigateBack() }

    }

    private fun unFocusButton(button: Button) {
        button.setTextColor(getColor(R.color.colorPrimary))
        button.setBackgroundResource(R.drawable.map_setttings_item_bg)
    }

    private fun focusButton(button: Button) {
        button.setTextColor(getColor(R.color.white))
        button.setBackgroundResource(R.drawable.filter_bg_filled)
    }

    override fun render(state: DetailsMapState) {
        mapTypeButtons.forEach { unFocusButton(it.button) }

        val focusedButton = mapTypeButtons.find { it.type == state.mapType }
        focusedButton?.let { focusButton(it.button) }

        googleMap?.mapType = state.mapType.value
    }

    data class Params(val latlng: List<Double>, val countryName: String? = null) : ScreenParams

    private data class MapTypeButton(
        val button: Button,
        val type: MapType
    )

}