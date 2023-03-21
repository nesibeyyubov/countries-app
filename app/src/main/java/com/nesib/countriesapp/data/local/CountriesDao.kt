package com.nesib.countriesapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nesib.countriesapp.models.CountryUi


@Dao
interface CountriesDao {

    @Query("SELECT * FROM countries")
    suspend fun getAllCountries(): List<CountryUi>

    @Query("SELECT * FROM countries WHERE region = :region")
    suspend fun getCountriesByRegion(region: String): List<CountryUi>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCountries(countries: List<CountryUi>)

}