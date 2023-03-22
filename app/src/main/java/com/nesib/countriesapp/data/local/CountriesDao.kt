package com.nesib.countriesapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nesib.countriesapp.models.BorderUi
import com.nesib.countriesapp.models.CountryUi


@Dao
interface CountriesDao {

    @Query("SELECT * FROM countries")
    suspend fun getAllCountries(): List<CountryUi>

    @Query("SELECT * FROM countries WHERE region = :region")
    suspend fun getCountriesByRegion(region: String): List<CountryUi>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCountries(countries: List<CountryUi>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBorders(borders: List<BorderUi>)

    @Query("SELECT * FROM borders WHERE key IN (:keys)")
    suspend fun getBorders(keys: List<String>): List<BorderUi>

}