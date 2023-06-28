package com.nesib.countriesapp

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.material.snackbar.Snackbar
import com.nesib.countriesapp.databinding.ActivityMainBinding
import com.nesib.countriesapp.ui.ConnectivityState
import com.nesib.countriesapp.ui.NetworkConnectivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModels<NetworkConnectivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment

        NavigationUI.setupWithNavController(
            navigationBarView = binding.navView,
            navController = navHostFragment.navController,
            saveState = false
        )


        observeConnectivity()

        if (!isGooglePlayServicesAvailable()) {
            finish()
        }
    }

    fun showCustomSnackbar(message: String, textColor: Int, backgroundColor: Int) {
        val snackbar = Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
        snackbar.setAnchorView(binding.navView.id)
        snackbar.view.setBackgroundColor(backgroundColor)
        val snackbarTv = snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        snackbarTv.setTextColor(textColor)
        snackbar.show()
    }

    private fun observeConnectivity() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            viewModel.connectivityStatus
                .onEach {
                    when (it.status) {
                        ConnectivityState.Status.Unavailable -> showCustomSnackbar(
                            getString(R.string.network_alert_unavailable),
                            Color.WHITE,
                            getColor(R.color.warning_color)
                        )
                        ConnectivityState.Status.Available -> showCustomSnackbar(
                            getString(R.string.network_alert_available),
                            Color.WHITE,
                            getColor(R.color.success_color)
                        )
                        ConnectivityState.Status.Unknown -> showCustomSnackbar(
                            getString(R.string.network_alert_unavailable),
                            Color.WHITE,
                            getColor(R.color.warning_color)
                        )
                    }
                }
                .flowWithLifecycle(lifecycle, Lifecycle.State.RESUMED)
                .launchIn(lifecycleScope)
        }

    }

    fun showBottomNav(show: Boolean) {
        binding.navView.isVisible = show
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