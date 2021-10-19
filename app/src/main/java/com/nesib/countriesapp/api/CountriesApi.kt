package com.nesib.countriesapp.api

import retrofit2.http.GET
import com.nesib.countriesapp.models.Country
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Path

interface CountriesApi {
    @GET("region/{region}")
    suspend fun getCountriesByRegion(@Path("region") region: String?): Response<List<Country>>

    @GET("name/{name}")
    suspend fun getCountriesByName(@Path("name") name: String?): Response<List<Country>>

    @GET("all")
    suspend fun getAllCountries(): Response<List<Country>>
}