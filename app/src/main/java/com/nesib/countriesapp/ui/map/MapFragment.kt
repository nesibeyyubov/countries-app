package com.nesib.countriesapp.ui.map

import android.graphics.Color
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.GoogleMap
import android.widget.ImageButton
import androidx.navigation.NavController
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.nesib.countriesapp.R
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.CameraUpdateFactory

class MapFragment : Fragment(R.layout.fragment_map), View.OnClickListener {
    private lateinit var mapView: MapView
    private lateinit var mGoogleMap: GoogleMap
    private lateinit var latlng: FloatArray
    private lateinit var normal: Button
    private lateinit var satellite: Button
    private lateinit var hybrid: Button
    private lateinit var terrain: Button
    private lateinit var focusedButton: Button
    private lateinit var backButton: ImageButton
    private lateinit var settingButtons:MutableList<Button>
    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = view.findViewById(R.id.mapView)
        normal = view.findViewById(R.id.normalBtn)
        hybrid = view.findViewById(R.id.hybridBtn)
        terrain = view.findViewById(R.id.terrainBtn)
        satellite = view.findViewById(R.id.satelliteBtn)
        backButton = view.findViewById(R.id.backButton)
        navController = Navigation.findNavController(view)
        settingButtons = mutableListOf(normal, satellite, terrain, hybrid)
        for (btn in settingButtons) {
            btn.setOnClickListener(this)
        }
        focusedButton = settingButtons[0]
        focusedButton.setTextColor(resources.getColor(R.color.colorPrimary))
        focusedButton.background =
            ResourcesCompat.getDrawable(resources, R.drawable.map_setttings_item_bg_filled, null)
        backButton.setOnClickListener(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        MapsInitializer.initialize(activity)
        mapView!!.onCreate(savedInstanceState)
        assert(arguments != null)
        latlng = MapFragmentArgs.fromBundle(requireArguments()).latLng!!
        mapView!!.getMapAsync { googleMap ->
            mGoogleMap = googleMap
            val markerOptions = MarkerOptions()
            val latLng = LatLng(latlng[0].toDouble(), latlng[1].toDouble())
            markerOptions.position(latLng)
            googleMap.addMarker(markerOptions)
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5f))
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView!!.onSaveInstanceState(outState)
    }

    override fun onResume() {
        super.onResume()
        mapView!!.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView!!.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView!!.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView!!.onLowMemory()
    }

    override fun onClick(view: View) {
        var sameButtonPressed = false
        when (view.id) {
            R.id.normalBtn -> {
                sameButtonPressed = changeButtonFocusTo(settingButtons[0])
                if (!sameButtonPressed) {
                    mGoogleMap!!.mapType = GoogleMap.MAP_TYPE_NORMAL
                }
            }
            R.id.satelliteBtn -> {
                sameButtonPressed = changeButtonFocusTo(settingButtons[1])
                if (!sameButtonPressed) {
                    mGoogleMap!!.mapType = GoogleMap.MAP_TYPE_SATELLITE
                }
            }
            R.id.terrainBtn -> {
                sameButtonPressed = changeButtonFocusTo(settingButtons[2])
                if (!sameButtonPressed) {
                    mGoogleMap!!.mapType = GoogleMap.MAP_TYPE_TERRAIN
                }
            }
            R.id.hybridBtn -> {
                sameButtonPressed = changeButtonFocusTo(settingButtons[3])
                if (!sameButtonPressed) {
                    mGoogleMap!!.mapType = GoogleMap.MAP_TYPE_HYBRID
                }
            }
            R.id.backButton -> navController!!.popBackStack()
        }
    }

    private fun changeButtonFocusTo(buttonToFocus: Button?): Boolean {
        if (focusedButton === buttonToFocus) {
            return true
        }
        buttonToFocus!!.setTextColor(resources.getColor(R.color.colorPrimary))
        buttonToFocus.background =
            ResourcesCompat.getDrawable(resources, R.drawable.map_setttings_item_bg_filled, null)
        focusedButton!!.setTextColor(Color.WHITE)
        focusedButton!!.background =
            ResourcesCompat.getDrawable(resources, R.drawable.map_setttings_item_bg, null)
        focusedButton = buttonToFocus
        return false
    }
}