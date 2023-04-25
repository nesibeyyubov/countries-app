package com.nesib.countriesapp.utils

object Constants {

    // Api
    const val API_BASE_URL = "https://restcountries.com/v3.1/"

    const val QUIZ_DURATION = 60 * 1000

    // Score
    const val SCORE_KEY_FLAGS = "bestScoreFlags"
    const val SCORE_KEY_REGIONS = "bestScoreRegions"
    const val SCORE_KEY_CAPITALS = "bestScoreCapitals"

    val NECESSARY_APİ_FİELDS = listOf(
        "name",
        "tld",
        "currencies",
        "idd",
        "capital",
        "altSpellings",
        "region",
        "subregion",
        "languages",
        "latlng",
        "borders",
        "area",
        "population",
        "car",
        "timezones",
        "continents",
        "flags",
        "coatOfArms",
        "postalCode",
        "startOfWeek",
        "cca3"
    ).joinToString(",")

    val BORDER_COUNTRIES_FIELDS = listOf(
        "name",
        "flags",
        "cca3"
    ).joinToString(",")
}