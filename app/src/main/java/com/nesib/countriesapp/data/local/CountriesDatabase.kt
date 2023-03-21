package com.nesib.countriesapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nesib.countriesapp.models.CountryUi


@Database(entities = [CountryUi::class], version = 1)
@androidx.room.TypeConverters(value = [TypeConverters::class])
abstract class CountriesDatabase : RoomDatabase() {
    abstract fun countriesDao(): CountriesDao
}