package com.floveit.weatherwidget.data.location

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.util.Log
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class LocationHelper(private val context: Context) {

    fun hasLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    fun requestLocationPermissions(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            1001
        )
    }

    suspend fun getCityFromLocation(): String? = withContext(Dispatchers.IO) {
        val location = getLastKnownLocation() ?: return@withContext null
        reverseGeocodeCity(location.latitude, location.longitude)
    }

    private fun getLastKnownLocation(): Location? {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val hasFine = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val hasCoarse = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

        if (!hasFine && !hasCoarse) {
            Log.e("LocationHelper", "❌ No location permissions granted.")
            return null
        }

        val providers = locationManager.getProviders(true)
        Log.d("LocationHelper", "📡 Available providers: $providers")

        for (provider in providers.reversed()) { // Try network first
            val location = locationManager.getLastKnownLocation(provider)
            Log.d("LocationHelper", "🔍 Location from $provider: $location")
            if (location != null) return location
        }

        Log.e("LocationHelper", "❌ No usable location found from any provider.")
        return null
    }

//    private fun getLastKnownLocation(): Location? {
//        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
//        val provider = LocationManager.NETWORK_PROVIDER
//        val location = locationManager.getLastKnownLocation(provider)
//        Log.d("LocationHelper", "📍 Forced network location = $location")
//        return location
//    }

//    private fun reverseGeocodeCity(lat: Double, lon: Double): String? {
//        val url = "https://nominatim.openstreetmap.org/reverse?lat=$lat&lon=$lon&format=json"
//        val client = OkHttpClient()
////        val request = Request.Builder()
////            .url(url)
////            .header("User-Agent", "FloveitWeatherApp")
////            .build()
//        val request = Request.Builder()
//            .url(url)
//            .header("User-Agent", "FloveitWeatherApp/1.0 (debashis842@gmail.com)") // ← Nominatim requires this!
//            .build()
//
//        return try {
//            val response = client.newCall(request).execute()
//            val json = JSONObject(response.body?.string() ?: return null)
//            val address = json.optJSONObject("address")
//            address?.optString("city")
//                ?: address?.optString("town")
//                ?: address?.optString("village")
//        } catch (e: Exception) {
//            null
//        }
//    }
    private fun reverseGeocodeCity(lat: Double, lon: Double): String? {
        val url = "https://nominatim.openstreetmap.org/reverse?lat=$lat&lon=$lon&format=json"
        val client = OkHttpClient()

        Log.d("LocationHelper", "📤 Sending reverse geocode request to: $url")

        val request = Request.Builder()
            .url(url)
            .header("User-Agent", "FloveitWeatherApp/1.0 (debashis842@gmail.com)")
            .build()

        return try {
            val response = client.newCall(request).execute()

            // Log HTTP response code
            Log.d("LocationHelper", "🌐 HTTP response code: ${response.code}")

            // Read response body
            val raw = response.body?.string()
//            Log.d("LocationHelper", "🌐 Raw response body:\n$raw")

            // If the response is not HTTP 200, abort
            if (response.code != 200) {
                Log.e("LocationHelper", "❌ Nominatim returned HTTP ${response.code}")
                return null
            }

            val json = JSONObject(raw ?: return null)
            val address = json.optJSONObject("address")

            val city = listOf(
                address?.optString("city", null),
                address?.optString("town", null),
                address?.optString("suburb", null),
                address?.optString("village", null),
                address?.optString("county", null)
            ).firstOrNull { !it.isNullOrBlank() }

            Log.d("LocationHelper", "🏙️ Resolved city name: ${city ?: "null"}")

            return city
        } catch (e: Exception) {
            Log.e("LocationHelper", "❌ Exception during reverse geocoding: ${e.localizedMessage}", e)
            return null
        }
    }
}