package com.floveit.weatherwidget.data.location

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray


class LocationRepositoryImpl : LocationRepository {
    private val client = OkHttpClient()

    override suspend fun getSuggestions(query: String): List<PlaceSuggestion> = withContext(Dispatchers.IO) {
        if (query.isBlank()) return@withContext emptyList()

        val url = "https://api.weatherapi.com/v1/search.json?key=139850d338ae46aaafa62608253107&q=$query"
        val request = Request.Builder().url(url).build()
        val response = client.newCall(request).execute()
        val raw = response.body?.string() ?: return@withContext emptyList()

        // WeatherAPI returns an array of location objects (or an error object)
        if (raw.contains("\"error\"")) return@withContext emptyList()

        val arr = JSONArray(raw)
        val out = ArrayList<PlaceSuggestion>(arr.length())
        for (i in 0 until arr.length()) {
            val o = arr.getJSONObject(i)
            val name = o.optString("name")
            val region = o.optString("region")
            val country = o.optString("country")
            val lat = o.optDouble("lat")
            val lon = o.optDouble("lon")

            val display = listOf(name, region, country)
                .filter { it.isNotBlank() }
                .joinToString(", ")

            out += PlaceSuggestion(
                displayName = display,
                lat = lat,
                lon = lon
            )
        }
        out
    }
}