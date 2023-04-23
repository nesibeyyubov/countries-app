package com.nesib.countriesapp.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.nesib.countriesapp.base.BaseAndroidViewModel
import com.nesib.countriesapp.base.State
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.N)
@HiltViewModel
class NetworkConnectivityViewModel @Inject constructor(
    myApplication: Application
) :
    BaseAndroidViewModel<ConnectivityState>(ConnectivityState(), myApplication) {

    private var connectivityCallback: ConnectivityManager.NetworkCallback? = null

    private val connectivityManager =
        myApplication.applicationContext?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager


    init {
        connectivityCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                Log.d("mytag", "available: ")
                setState { it.copy(status = ConnectivityState.Status.Available) }
            }

            override fun onUnavailable() {
                super.onUnavailable()
                Log.d("mytag", "onUnavailable: ")
                setState { it.copy(status = ConnectivityState.Status.Unavailable) }
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                setState { it.copy(status = ConnectivityState.Status.Unavailable) }
            }
        }
        connectivityCallback?.let { connectivityManager.registerDefaultNetworkCallback(it) }
    }

    override fun onCleared() {
        super.onCleared()
        connectivityCallback?.let { connectivityManager.unregisterNetworkCallback(it) }
    }

}


data class ConnectivityState(val status: Status = Status.Unknown) : State {

    enum class Status { Available, Unavailable, Unknown }
}

