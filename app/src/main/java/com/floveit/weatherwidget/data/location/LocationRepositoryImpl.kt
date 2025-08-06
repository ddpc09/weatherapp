package com.floveit.weatherwidget.data.location

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray

class LocationRepositoryImpl : LocationRepository {
    override suspend fun getSuggestions(query: String): List<String> = withContext(Dispatchers.IO) {
        if (query.isBlank()) return@withContext emptyList()

        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://api.weatherapi.com/v1/search.json?key=139850d338ae46aaafa62608253107&q=$query")
            .build()

        val response = client.newCall(request).execute()
        val raw = response.body?.string() ?: return@withContext emptyList()

        // Safely check for error
        if (raw.contains("\"error\"")) return@withContext emptyList()

        val jsonArray = JSONArray(raw)
        val list = mutableListOf<String>()

        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            val name = obj.getString("name")
            val region = obj.optString("region")
            val country = obj.optString("country")
            list.add("$name, $region, $country".replace(Regex(",\\s*,"), ",").trimEnd(','))
        }

        list
    }
}