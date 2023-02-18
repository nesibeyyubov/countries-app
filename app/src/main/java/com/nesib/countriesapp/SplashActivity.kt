package com.nesib.countriesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Toast
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.ConnectionResult

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        if (isGooglePlayServicesAvailable()) {
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

    private fun isGooglePlayServicesAvailable(): Boolean {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val status = googleApiAvailability.isGooglePlayServicesAvailable(this)
        if (status != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(this, status, 2404)?.show()
            }
            return false
        }
        return true
    }
}