package com.nesib.countriesapp.data.network

import com.nesib.countriesapp.models.CountryModel
import com.nesib.countriesapp.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CountriesApi {

    @GET("alpha")
    suspend fun getCountriesByAlphaCode(
        @Query("codes") codes: String,
        @Query("fields") fields: String
    ): List<CountryModel>

    @GET("all")
    suspend fun getAllCountries(@Query("fields") necessaryFields: String = Constants.NECESSARY_APİ_FİELDS): List<CountryModel>

    @GET("region/{regionName}")
    suspend fun getCountriesByRegion(
        @Path("regionName") region: String,
        @Query("fields") necessaryFields: String = Constants.NECESSARY_APİ_FİELDS
    ): List<CountryModel>

}