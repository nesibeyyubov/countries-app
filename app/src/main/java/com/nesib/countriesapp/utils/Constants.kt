package com.nesib.countriesapp.utils

object Constants {
    // Api
    const val API_BASE_URL = "https://restcountries.com/v3.1/"

    // Database
    const val DB_NAME = "countries_db"
    const val TABLE_NAME = "countries_table"

    // Columns
    const val COL_NAME = "name"
    const val COL_CAPITAL = "capital"
    const val COL_LAT = "lat"
    const val COL_LNG = "lng"
    const val COL_REGION = "region"
    const val COL_POPULATION = "population"
    const val COL_AREA = "area"
    const val COL_FLAG = "flag"
    const val COL_CALLING_CODE = "callingCodes"
    const val COL_BORDERS = "borders"
    const val COL_CURRENCIES = "currencies"
    const val COL_LANGUAGES = "languages"
    const val COL_ALPHA3_CODE = "alpha3Code"
    const val COL_FULL_NAME = "altSpellings"

    // Quiz
    const val QUIZ_TYPE_REGION = "regions"
    const val QUIZ_TYPE_FLAGS = "flags"
    const val QUIZ_TYPE_CAPITALS = "capitals"

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