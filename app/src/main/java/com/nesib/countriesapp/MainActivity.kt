package com.nesib.countriesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        val navView = findViewById<BottomNavigationView>(R.id.nav_view)
//        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
//        navView.setupWithNavController(navController)
    }
}