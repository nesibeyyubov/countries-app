package com.nesib.countriesapp.ui.countries

import androidx.lifecycle.viewModelScope
import com.nesib.countriesapp.R
import com.nesib.countriesapp.base.BaseViewModel
import com.nesib.countriesapp.data.network.CountriesRepository
import com.nesib.countriesapp.utils.NetworkNotAvailableException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class CountriesViewModel @Inject constructor(
    private val countriesRepository: CountriesRepository,
) : BaseViewModel<CountriesState>(CountriesState()) {

    fun sortCountriesBy(sortBy: SingleChipSelector.SortBy) {
        setState { it.copy(sortedBy = sortBy) }
    }

    fun search(query: String) {
        setState { it.copy(searchQuery = query) }
    }

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

    fun getAllCountries() {
        if (currentState().countries.isNotEmpty()) {
            return
        }
        countriesRepository.getAllCountries()
            .onStart {
                setState { it.copy(loading = true) }
            }
            .catch {
                handleError(it)
            }
            .onEach { countries ->
                setState { it.copy(loading = false, countries = countries) }
            }.flowOn(Dispatchers.IO).launchIn(viewModelScope)
    }

    fun getCountriesByRegion(region: String) {
        if (currentState().countries.isNotEmpty()) {
            return
        }
        countriesRepository.getCountryByRegion(region)
            .onStart {
                setState { it.copy(loading = true) }
            }
            .catch {
                handleError(it)
            }
            .onEach { countries ->
                setState { currentState -> currentState.copy(loading = false, countries = countries) }
            }.flowOn(Dispatchers.IO).launchIn(viewModelScope)

    }

}