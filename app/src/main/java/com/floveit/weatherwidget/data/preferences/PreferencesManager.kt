package com.floveit.weatherwidget.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.datastore.preferences.preferencesDataStore

private val Context.dataStore by preferencesDataStore(name = "user_preferences")

class PreferencesManager(private val context: Context) {

    companion object {
        private val LAST_LOCATION_KEY = stringPreferencesKey("last_location")
    }

    val lastLocationFlow: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[LAST_LOCATION_KEY] }

    suspend fun saveLastLocation(city: String) {
        context.dataStore.edit { preferences ->
            preferences[LAST_LOCATION_KEY] = city
        }
    }
}