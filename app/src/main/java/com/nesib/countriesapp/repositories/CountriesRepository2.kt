package com.nesib.countriesapp.repositories

import com.nesib.countriesapp.ApiService
import com.nesib.countriesapp.api.CountriesApi
import com.nesib.countriesapp.models.Country
import retrofit2.Response
import retrofit2.Retrofit

object CountriesRepository2 {
    suspend fun getAllCountries(): Response<List<Country>> = ApiService.api.getAllCountries()

    suspend fun getCountriesByName(name: String): Response<List<Country>> =
        ApiService.api.getCountriesByName(name)

    suspend fun getCountriesByRegion(region: String): Response<List<Country>> =
        ApiService.api.getCountriesByRegion(region)
}