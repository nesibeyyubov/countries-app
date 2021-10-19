package com.nesib.countriesapp.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nesib.countriesapp.DataState
import com.nesib.countriesapp.models.Country
import com.nesib.countriesapp.repositories.CountriesRepository2
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception

class CountriesViewModel2 : ViewModel() {
    private val _countriesByRegion = MutableLiveData<DataState<List<Country>>>()
    val countriesByRegion: LiveData<DataState<List<Country>>>
        get() = _countriesByRegion

    private val _countriesByName = MutableLiveData<DataState<List<Country>>>()
    val countriesByName: LiveData<DataState<List<Country>>>
        get() = _countriesByName

    private val _allCountries = MutableLiveData<DataState<List<Country>>>()
    val allCountries: LiveData<DataState<List<Country>>>
        get() = _allCountries


    fun getAllCountries() = viewModelScope.launch {
        try {
            _allCountries.postValue(DataState.Loading())
            val response = CountriesRepository2.getAllCountries()
            val handledResponse = handleResponse(response)
            _allCountries.postValue(handledResponse)
        } catch (e: Exception) {
            _allCountries.postValue(DataState.Error(message = "From catch block"))
        }
    }

    fun getCountriesByRegion(region: String) = viewModelScope.launch {
        try {
            _countriesByRegion.postValue(DataState.Loading())
            val response = CountriesRepository2.getCountriesByRegion(region)
            val handledResponse = handleResponse(response)
            _countriesByRegion.postValue(handledResponse)
        } catch (e: Exception) {
            _countriesByRegion.postValue(DataState.Error(message = "From catch block"))
        }
    }

    fun getCountriesByName(name: String) = viewModelScope.launch {
        try {
            _countriesByName.postValue(DataState.Loading())
            val response = CountriesRepository2.getCountriesByName(name)
            val handledResponse = handleResponse(response)
            _countriesByName.postValue(handledResponse)
        } catch (e: Exception) {
            _countriesByName.postValue(DataState.Error(message = "From catch block"))
        }
    }

    private fun handleResponse(response: Response<List<Country>>): DataState<List<Country>> {
        when (response.code()) {
            200 -> {
                return DataState.Success(data = response.body() ?: emptyList())
            }
            404 -> {
                return DataState.Error(message = "404 error")
            }
            500 -> {
                return DataState.Error(message = "500 error")
            }
            401 -> {
                return DataState.Error(message = "401 error")
            }
            201 -> {
                return DataState.Error(message = "201 error")
            }
            else ->{
                return DataState.Error(message = "Not handled error code")
            }
        }
    }


}