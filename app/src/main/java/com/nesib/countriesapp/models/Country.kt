package com.nesib.countriesapp.models

import java.io.Serializable

data class Country(
    var name: String? = null,
    var capital: String? = null,
    var region: String? = null,
    var population: Long = 0,
    var area: Double = 0.0,
    var flag: String? = null,
    var alpha3Code: String? = null,
    var altSpellings: List<String> = emptyList(),
    var callingCodes: List<String> = emptyList(),
    var latlng: List<Double> = emptyList(),
    var borders: List<String> = emptyList(),
    var currencies: List<Currency> = emptyList(),
    var languages: List<Language> = emptyList(),
) : Serializable