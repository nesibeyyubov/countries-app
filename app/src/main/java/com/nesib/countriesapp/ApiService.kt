package com.nesib.countriesapp

import retrofit2.Retrofit
import com.nesib.countriesapp.ApiService
import com.nesib.countriesapp.api.CountriesApi
import retrofit2.converter.gson.GsonConverterFactory

object ApiService {
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(Constants.API_BASE_URL)
        .build()

    val api = retrofit.create(CountriesApi::class.java)
}