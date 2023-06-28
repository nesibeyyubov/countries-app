package com.nesib.countriesapp.di

import android.content.Context
import androidx.room.Room
import com.nesib.countriesapp.data.local.CountriesDao
import com.nesib.countriesapp.data.local.CountriesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext context: Context): CountriesDatabase {
        return Room
            .databaseBuilder(context, CountriesDatabase::class.java, "countries")
            .build()
    }

    @Provides
    @Singleton
    fun provideDao(countriesDatabase: CountriesDatabase): CountriesDao {
        return countriesDatabase.countriesDao()
    }

}