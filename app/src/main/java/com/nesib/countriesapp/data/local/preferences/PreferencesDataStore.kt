package com.nesib.countriesapp.data.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.nesib.countriesapp.utils.Constants
import com.nesib.countriesapp.utils.dataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferencesDataStore @Inject constructor(@ApplicationContext context: Context) {

    private val dataStore: DataStore<Preferences> = context.dataStore

    private val capitalsKey = intPreferencesKey(Constants.SCORE_KEY_CAPITALS)
    private val flagsKey = intPreferencesKey(Constants.SCORE_KEY_FLAGS)
    private val regionsKey = intPreferencesKey(Constants.SCORE_KEY_REGIONS)


    fun getScorePreferences(): Flow<ScorePreference> {
        return dataStore.data.map { preferences ->
            val capitals = preferences[capitalsKey] ?: 0
            val regions = preferences[regionsKey] ?: 0
            val flags = preferences[flagsKey] ?: 0

            ScorePreference(
                capitalsScore = capitals,
                flagsScore = flags,
                regionsScore = regions
            )
        }
    }

    suspend fun setCapitalsScore(newScore: Int) {
        dataStore.edit { preferences ->
            preferences[capitalsKey] = newScore
        }
    }

    suspend fun setRegionsScore(newScore: Int) {
        dataStore.edit { preferences ->
            preferences[regionsKey] = newScore
        }
    }

    suspend fun setFlagsScore(newScore: Int) {
        dataStore.edit { preferences ->
            preferences[flagsKey] = newScore
        }
    }


}


data class ScorePreference(
    val capitalsScore: Int,
    val flagsScore: Int,
    val regionsScore: Int,
)