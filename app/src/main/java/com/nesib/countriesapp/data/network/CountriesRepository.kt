package com.nesib.countriesapp.data.network

import com.nesib.countriesapp.data.local.CountriesDao
import com.nesib.countriesapp.models.CountryUi
import com.nesib.countriesapp.models.toUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CountriesRepository @Inject constructor(
    private val dao: CountriesDao,
    private val countriesApi: CountriesApi,
) {

    fun getAllCountries(): Flow<List<CountryUi>> = flow {
        val countriesInDb = dao.getAllCountries()
        if (countriesInDb.isNotEmpty()) {
            emit(countriesInDb)
        } else {
            val countries = countriesApi.getAllCountries()
            emit(countries.map { countryModel ->
                countryModel.toUiModel()
            })
            dao.insertAllCountries(countries.map { it.toUiModel() })
        }

    }

    fun getCountryByRegion(region: String): Flow<List<CountryUi>> = flow {
        val countriesInDb = dao.getCountriesByRegion(region.capitalize(Locale.ROOT))
        if (countriesInDb.isNotEmpty()) {
            emit(countriesInDb)
        } else {
            emit(countriesApi.getCountriesByRegion(region).map { countryModel -> countryModel.toUiModel() })
        }
    }


}