package com.nesib.countriesapp.data.network

import com.nesib.countriesapp.models.CountryModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CountriesApiImpl @Inject constructor(
    private val api: CountriesApi,
) {

    fun getAllCountries(): Flow<List<CountryModel>> = flow {
        val countries = api.getAllCountries()
        emit(countries)
    }

    fun getCountriesByRegion(region: String): Flow<List<CountryModel>> = flow {
        emit(api.getCountriesByRegion(region))
    }
}