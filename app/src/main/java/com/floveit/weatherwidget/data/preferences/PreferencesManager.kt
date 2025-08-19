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
        // Legacy single key (kept for migration)
        private val LAST_LOCATION_LEGACY = stringPreferencesKey("last_location")

        // New keys
        private val LAST_LOCATION_QUERY = stringPreferencesKey("last_location_query")   // what we send to API (lat,lon)
        private val LAST_LOCATION_LABEL = stringPreferencesKey("last_location_label")   // what we show in UI
    }

    /** What we send to Weather API (prefer new key; fall back to legacy if needed) */
    val lastLocationQueryFlow: Flow<String?> = context.dataStore.data.map { p ->
        p[LAST_LOCATION_QUERY] ?: p[LAST_LOCATION_LEGACY]
    }

    /** What we show in UI */
    val lastLocationLabelFlow: Flow<String?> = context.dataStore.data.map { p ->
        p[LAST_LOCATION_LABEL]
    }

    /** Save both values atomically */
    suspend fun saveLastLocation(query: String, label: String) {
        context.dataStore.edit { p ->
            p[LAST_LOCATION_QUERY] = query
            p[LAST_LOCATION_LABEL] = label
        }
    }
}
