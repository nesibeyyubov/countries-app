package com.nesib.countriesapp.ui.countries

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.nesib.countriesapp.base.BaseViewModel
import com.nesib.countriesapp.data.network.CountriesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class CountriesViewModel @Inject constructor(
    private val countriesRepository: CountriesRepository
) : BaseViewModel<CountriesState>(CountriesState()) {


    fun getAllCountries() {
        countriesRepository.getAllCountries()
            .onStart {
                if (currentState().countries.isNotEmpty()) {
                    emit(currentState().countries)
                } else {
                    setState { it.copy(loading = true) }
                }
            }
            .catch {
            }
            .onEach { countries ->
                setState { it.copy(loading = false, countries = countries) }
            }
            .flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)
    }

    fun getCountriesByRegion(region: String) {
        countriesRepository.getCountryByRegion(region)
            .onStart {
                if (currentState().countries.isNotEmpty()) {
                    emit(currentState().countries)
                } else {
                    setState { it.copy(loading = true) }
                }
            }
            .catch {
            }
            .onEach { countries ->
                setState { currentState -> currentState.copy(loading = false, countries = countries) }
            }
            .flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)

    }

}