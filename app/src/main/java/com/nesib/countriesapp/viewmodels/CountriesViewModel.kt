package com.nesib.countriesapp.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nesib.countriesapp.utils.DataState
import com.nesib.countriesapp.utils.SortOptions
import com.nesib.countriesapp.models.Country
import com.nesib.countriesapp.repositories.CountriesRepository
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception

class CountriesViewModel : ViewModel() {
    private val _countriesByRegion = MutableLiveData<DataState<List<Country>>>()
    val countriesByRegion: LiveData<DataState<List<Country>>>
        get() = _countriesByRegion

    private val _countriesByName = MutableLiveData<DataState<List<Country>>>()
    val countriesByName: LiveData<DataState<List<Country>>>
        get() = _countriesByName

    private val _countriesSortedByArea = MutableLiveData<List<Country>?>()
    val countriesSortedByArea: LiveData<List<Country>?>
        get() = _countriesSortedByArea

    private val _countriesSortedByPopulation = MutableLiveData<List<Country>?>()
    val countriesSortedByPopulation: LiveData<List<Country>?>
        get() = _countriesSortedByPopulation

    private val _countriesSortedBySearchInput = MutableLiveData<List<Country>?>()
    val countriesSortedBySearchInput: LiveData<List<Country>?>
        get() = _countriesSortedBySearchInput

    private val _allCountries = MutableLiveData<DataState<List<Country>>>()
    val allCountries: LiveData<DataState<List<Country>>>
        get() = _allCountries

    fun clearSortedCountries() {
        _countriesSortedByArea.value = null
        _countriesSortedByPopulation.value = null
        if (_countriesByName.value != null) {
            _countriesByName.value = _countriesByName.value
        } else {
            _countriesByRegion.value = _countriesByRegion.value
        }
    }

    fun sortCountriesByArea(searchText: String? = null) {
        val sortedCountries: List<Country>? = if (_countriesByName.value != null) {
            _countriesByName.value?.data?.let {
                it.sortedByDescending { country -> country.area }
            }
        } else {
            _countriesByRegion.value?.data?.let {
                if (searchText != null) {
                    it
                        .filter { country ->
                            country.name?.toLowerCase()?.contains(searchText.toLowerCase()) ?: false
                        }
                        .sortedByDescending { country -> country.area }
                } else {
                    it.sortedByDescending { country -> country.area }
                }
            }
        }
        sortedCountries?.let {
            _countriesSortedByArea.value = it
        }
    }

    fun sortCountriesByPopulation(searchText: String? = null) {
        val sortedCountries: List<Country>? = if (_countriesByName.value != null) {
            _countriesByName.value?.data?.let {
                it.sortedByDescending { country -> country.population }
            }
        } else {
            _countriesByRegion.value?.data?.let {
                if (searchText != null) {
                    it
                        .filter { country ->
                            country.name?.toLowerCase()?.contains(searchText.toLowerCase()) ?: false
                        }
                        .sortedByDescending { country -> country.population }
                } else {
                    it.sortedByDescending { country -> country.population }
                }

            }
        }
        sortedCountries?.let {
            _countriesSortedByPopulation.value = it
        }
    }

    fun sortCountriesBySearchInput(searchText: String, currentSortOption: SortOptions) {
        val sortedCountries: List<Country>? = when (currentSortOption) {
            SortOptions.None -> {
                _countriesByRegion.value?.data?.let {
                    it.filter { country ->
                        country.name?.toLowerCase()?.contains(searchText.toLowerCase()) ?: false
                    }
                }
            }
            SortOptions.Area -> {
                _countriesByRegion.value?.data?.let {
                    it.sortedByDescending { country -> country.population }.filter { country ->
                        country.name?.toLowerCase()?.contains(searchText.toLowerCase()) ?: false
                    }
                }
            }
            SortOptions.Population -> {
                _countriesByRegion.value?.data?.let {
                    it.sortedByDescending { country -> country.population }.filter { country ->
                        country.name?.toLowerCase()?.contains(searchText.toLowerCase()) ?: false
                    }
                }
            }
        }
        sortedCountries?.let {
            _countriesSortedBySearchInput.value = it
        }
    }


    fun getAllCountries() = viewModelScope.launch {
        try {
            _allCountries.postValue(DataState.Loading())
            val response = CountriesRepository.getAllCountries()
            val handledResponse = handleResponse(response)
            _allCountries.postValue(handledResponse)
        } catch (e: Exception) {
            if(e.message?.toLowerCase()?.contains("address") == true){
                _allCountries.postValue(DataState.Error(message = "No internet connection."))
            }else if(e.message?.toLowerCase()?.contains("expect") == true){
                _allCountries.postValue(DataState.Error(message = "Country not found."))
            }
        }
    }

    fun getCountriesByRegion(region: String) = viewModelScope.launch {
        try {
            _countriesByRegion.postValue(DataState.Loading())
            val response = CountriesRepository.getCountriesByRegion(region)
            val handledResponse = handleResponse(response)
            _countriesByRegion.postValue(handledResponse)
        } catch (e: Exception) {
            if(e.message?.toLowerCase()?.contains("address") == true){
                _countriesByRegion.postValue(DataState.Error(message = "No internet connection."))
            }else if(e.message?.toLowerCase()?.contains("expect") == true){
                _countriesByRegion.postValue(DataState.Error(message = "Country not found."))
            }
        }
    }

    fun getCountriesByName(name: String) = viewModelScope.launch {
        try {
            _countriesByName.postValue(DataState.Loading())
            val response = CountriesRepository.getCountriesByName(name)
            val handledResponse = handleResponse(response)
            _countriesByName.postValue(handledResponse)
        } catch (e: Exception) {
            if(e.message?.toLowerCase()?.contains("address") == true){
                _countriesByName.postValue(DataState.Error(message = "No internet connection."))
            }else if(e.message?.toLowerCase()?.contains("expect") == true){
                _countriesByName.postValue(DataState.Error(message = "Country not found."))
            }
        }
    }

    private fun handleResponse(response: Response<List<Country>>): DataState<List<Country>> {
        when (response.code()) {
            200 -> {
                return DataState.Success(data = response.body() ?: emptyList())
            }
            404 -> {
                return DataState.Error(message = "Error: Not found.")
            }
            500 -> {
                return DataState.Error(message = "There is a problem with server.\nTry Again later")
            }
            401 -> {
                return DataState.Error(message = "Unauthenticated.")
            }
            else -> {
                return DataState.Error(message = "Something went wrong")
            }
        }
    }


}