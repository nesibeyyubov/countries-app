package com.nesib.countriesapp

import android.app.Application
import android.util.Log
import com.google.android.gms.maps.MapsInitializer
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CountriesApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        MapsInitializer.initialize(this, MapsInitializer.Renderer.LATEST) {}
    }
}