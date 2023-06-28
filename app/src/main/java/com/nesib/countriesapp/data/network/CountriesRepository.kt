package com.nesib.countriesapp.data.network

import android.content.Context
import com.nesib.countriesapp.data.local.CountriesDao
import com.nesib.countriesapp.models.BorderUi
import com.nesib.countriesapp.models.CountryUi
import com.nesib.countriesapp.models.toBorderModel
import com.nesib.countriesapp.models.toUiModel
import com.nesib.countriesapp.utils.Constants
import com.nesib.countriesapp.utils.NetworkNotAvailableException
import com.nesib.countriesapp.utils.isNetworkAvailable
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CountriesRepository @Inject constructor(
    private val dao: CountriesDao,
    private val countriesApi: CountriesApi,
    @ApplicationContext private val context: Context
) {

    fun getBordersByAlphaCode(codes: List<String>): Flow<List<BorderUi>> = flow {
        val countriesInDb = dao.getBorders(codes)
        if (countriesInDb.isNotEmpty()) {
            emit(countriesInDb)
        } else {
            if (context.isNetworkAvailable().not()) {
                throw NetworkNotAvailableException()
            }
            val countries =
                countriesApi.getCountriesByAlphaCode(codes.joinToString(","), Constants.BORDER_COUNTRIES_FIELDS)
            val borders = countries.map { it.toBorderModel() }
            emit(borders)
            dao.insertBorders(borders)
        }

    }

    fun getAllCountries(): Flow<List<CountryUi>> = flow {
        val countriesInDb = dao.getAllCountries()
        if (countriesInDb.isNotEmpty()) {
            emit(countriesInDb)
        } else {
            if (context.isNetworkAvailable().not()) {
                throw NetworkNotAvailableException()
            }
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
            if (context.isNetworkAvailable().not()) {
                throw NetworkNotAvailableException()
            }
            emit(countriesApi.getCountriesByRegion(region).map { countryModel -> countryModel.toUiModel() })
        }
    }


}