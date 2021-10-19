package com.nesib.countriesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nesib.countriesapp.R
import android.content.Intent
import com.nesib.countriesapp.MainActivity
import android.app.Activity
import android.widget.Toast
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.ConnectionResult

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        if (isGooglePlayServicesAvailable(this)) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(
                this,
                "Google play services not available,\nplease turn it on to use the app",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun isGooglePlayServicesAvailable(activity: Activity?): Boolean {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val status = googleApiAvailability.isGooglePlayServicesAvailable(activity)
        if (status != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(activity, status, 2404).show()
            }
            return false
        }
        return true
    }
}