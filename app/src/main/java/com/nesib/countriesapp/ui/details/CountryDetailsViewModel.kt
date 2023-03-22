package com.nesib.countriesapp.ui.details

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.nesib.countriesapp.base.BaseViewModel
import com.nesib.countriesapp.data.network.CountriesRepository
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


    fun getBorders(codes: List<String>) {
        if (currentState().borders.isNotEmpty()) return
        if (codes.isEmpty()) return
        repository.getBordersByAlphaCode(codes)
            .onStart { setState { it.copy(bordersLoading = true) } }
            .onEach { borderCountries ->
                setState { it.copy(bordersLoading = false, borders = borderCountries) }
            }
            .catch {
                Log.d("mytag", "catch block: ${it.message}")
            }
            .launchIn(viewModelScope)

    }

}