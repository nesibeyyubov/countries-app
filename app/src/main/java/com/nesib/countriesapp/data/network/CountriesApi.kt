package com.nesib.countriesapp.data.network

import com.nesib.countriesapp.models.CountryModel
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Path

interface CountriesApi {

    @GET("all")
    suspend fun getAllCountries(): List<CountryModel>

    @GET("region/{regionName}")
    suspend fun getCountriesByRegion(@Path("regionName") region: String): List<CountryModel>

}