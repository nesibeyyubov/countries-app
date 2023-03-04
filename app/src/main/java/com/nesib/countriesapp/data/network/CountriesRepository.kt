package com.nesib.countriesapp.data.network

import com.nesib.countriesapp.models.CountryUi
import com.nesib.countriesapp.models.toUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CountriesRepository @Inject constructor(
    private val api: CountriesApiImpl
) {

    fun getAllCountries(): Flow<List<CountryUi>> {
        return api.getAllCountries().map { countries -> countries.map { countryModel -> countryModel.toUiModel() } }
    }

    fun getCountryByRegion(region: String): Flow<List<CountryUi>> {
        return api.getCountriesByRegion(region)
            .map { countries -> countries.map { countryModel -> countryModel.toUiModel() } }
    }


}