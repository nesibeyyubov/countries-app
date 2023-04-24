package com.nesib.countriesapp.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewModelScope
import com.nesib.countriesapp.base.BaseAndroidViewModel
import com.nesib.countriesapp.base.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
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

    private val _connectivityChannel = Channel<ConnectivityState>()
    val connectivityStatus = _connectivityChannel.receiveAsFlow().distinctUntilChanged()

    private var lostConnectionBefore = false


    init {
        connectivityCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                if (lostConnectionBefore) {
                    viewModelScope.launch {
                        _connectivityChannel.send(ConnectivityState(ConnectivityState.Status.Available))
                    }
                }
            }

            override fun onUnavailable() {
                super.onUnavailable()
                viewModelScope.launch {
                    _connectivityChannel.send(ConnectivityState(ConnectivityState.Status.Unavailable))
                }
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                lostConnectionBefore = true
                viewModelScope.launch {
                    _connectivityChannel.send(ConnectivityState(ConnectivityState.Status.Unavailable))
                }
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

