package com.nesib.countriesapp.ui.details

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.nesib.countriesapp.R
import com.nesib.countriesapp.base.BaseViewModel
import com.nesib.countriesapp.data.network.CountriesRepository
import com.nesib.countriesapp.utils.NetworkNotAvailableException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class CountryDetailsViewModel @Inject constructor(
    private val repository: CountriesRepository
) : BaseViewModel<CountryDetailsState>(CountryDetailsState()) {


    private fun handleError(error: Throwable) {
        when (error) {
            is NetworkNotAvailableException -> {
                setState { it.copy(error = R.string.error_network_not_available) }
            }
            else -> {
                setState { it.copy(error = R.string.error_something_wron) }
            }
        }
    }

    fun getBorders(codes: List<String>) {
        if (currentState().borders.isNotEmpty()) return
        if (codes.isEmpty()) return
        repository.getBordersByAlphaCode(codes)
            .onStart { setState { it.copy(bordersLoading = true) } }
            .onEach { borderCountries ->
                setState { it.copy(bordersLoading = false, borders = borderCountries) }
            }
            .catch {
                handleError(it)
            }
            .launchIn(viewModelScope)

    }

}