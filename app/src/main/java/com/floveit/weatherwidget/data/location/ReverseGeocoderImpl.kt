package com.floveit.weatherwidget.data.location

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.util.Locale
import kotlin.coroutines.resume


class ReverseGeocoderImpl(
    private val context: Context,
    private val locale: Locale = Locale.getDefault()
) : ReverseGeocoder {

    override suspend fun labelFor(
        lat: Double,
        lon: Double,
        mode: ReverseGeocoder.LabelMode
    ): String? = withContext(Dispatchers.IO) {
        if (!Geocoder.isPresent()) return@withContext null
        val geocoder = Geocoder(context, locale)

        val address: Address? = try {
            if (Build.VERSION.SDK_INT >= 33) {
                suspendCancellableCoroutine { cont ->
                    geocoder.getFromLocation(lat, lon, 1, object : Geocoder.GeocodeListener {
                        override fun onGeocode(addresses: MutableList<Address>) {
                            cont.resume(addresses.firstOrNull())
                        }
                        override fun onError(errorMessage: String?) {
                            cont.resume(null)
                        }
                    })
                }
            } else {
                @Suppress("DEPRECATION")
                geocoder.getFromLocation(lat, lon, 1)?.firstOrNull()
            }
        } catch (_: Exception) {
            null
        }

        address?.let { buildLabel(it, mode) }
    }

    // ---- Label builders -----------------------------------------------------

    private fun buildLabel(a: Address, mode: ReverseGeocoder.LabelMode): String? {
        val country = a.countryName?.takeIf { it.isNotBlank() }
        val city = (a.locality ?: a.subAdminArea ?: a.adminArea)?.takeIf { it.isNotBlank() }
        val suburbRaw = a.subLocality?.takeIf { it.isNotBlank() }
        val suburb = suburbRaw?.takeUnless { looksTooMicro(it) }

        return when (mode) {
            ReverseGeocoder.LabelMode.City -> listOfNotNull(city, country)
                .distinct()
                .joinToStringOrNull()

            ReverseGeocoder.LabelMode.Suburb -> {
                // Prefer suburb→city→country but don't show micro strings.
                val head = (suburb ?: city)
                listOfNotNull(head, city, country)
                    .distinct()
                    .joinToStringOrNull()
            }

            ReverseGeocoder.LabelMode.Auto -> {
                // Best effort: (suburb or feature) → city → country
                val feature = a.featureName?.takeIf { it.isNotBlank() }?.takeUnless { looksTooMicro(it) }
                val head = suburb ?: feature ?: city
                listOfNotNull(head, city, country)
                    .distinct()
                    .joinToStringOrNull()
            }
        }
    }

    /** Filters out overly specific “micro” strings (blocks, sectors, ward nos, pure numbers, etc.). */
    private fun looksTooMicro(s: String): Boolean {
        val n = s.trim().lowercase(Locale.ROOT)
        if (n.length <= 3) return true
        if (n.any(Char::isDigit)) return true
        return MICRO_TERMS.any { term -> n.contains(term) }
    }

    private fun List<String>.joinToStringOrNull(): String? =
        if (isEmpty()) null else joinToString(", ")

    companion object {
        // tweak this list to your market
        private val MICRO_TERMS = listOf(
            "block ", "sector ", "phase ", "ward ", "lane ", "road ", "rd ", "st ", "street ", "ave ", "avenue ",
            "plot ", "tower ", "estate ", "park ", "apartment", "apt ", "floor ", "gali ", "para ", "mohalla "
        )
    }
}