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

    fun sortCountriesBy(sortBy: SingleChipSelector.SortBy) {
        setState { it.copy(sortedBy = sortBy) }
    }

    fun search(query: String) {
        setState { it.copy(searchQuery = query) }
    }

    fun getAllCountries() {
        if (currentState().countries.isNotEmpty()) {
            return
        }
        countriesRepository.getAllCountries()
            .onStart {
                setState { it.copy(loading = true) }
            }
            .catch {}
            .onEach { countries ->
                Log.d("mytag", "getAllCountries:[onEach] ")
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
            .catch {}
            .onEach { countries ->
                setState { currentState -> currentState.copy(loading = false, countries = countries) }
            }.flowOn(Dispatchers.IO).launchIn(viewModelScope)

    }

}