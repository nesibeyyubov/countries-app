package com.nesib.countriesapp.api

import retrofit2.Retrofit
import com.nesib.countriesapp.utils.Constants
import retrofit2.converter.gson.GsonConverterFactory

object ApiService {
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(Constants.API_BASE_URL)
        .build()

    val api: CountriesApi = retrofit.create(CountriesApi::class.java)
}