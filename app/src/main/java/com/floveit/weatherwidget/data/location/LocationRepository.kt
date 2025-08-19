package com.floveit.weatherwidget.data.location

interface LocationRepository {
    suspend fun getSuggestions(query: String): List<PlaceSuggestion>
}