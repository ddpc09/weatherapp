package com.floveit.weatherwidget.ui

fun getAnimationForCondition(code: Int, isDay: Boolean): String {
    return when (code) {
        // ☀️ Clear/Sunny
        1000 -> if (isDay) "sunny" else "clear_night"

        // 🌤️ Partly Cloudy
        1003 -> if (isDay) "partly_cloudy" else "night_cloudy"

        // ☁️ Cloudy / Overcast
        1006, 1009 -> if (isDay) "overcast" else "night_cloudy"

        // 🌫️ Mist / Fog
        1030, 1135, 1147 -> "mist_fog"

        // 🌧️ Rain-related
        in listOf(
            1063, 1072, 1150, 1153, 1168, 1171, 1180, 1183, 1186, 1189, 1192,
            1195, 1198, 1201, 1240, 1243, 1246
        ) -> if (isDay) "rainy" else "night_rainy"

        // ❄️ Snow / Ice / Blizzard
        in listOf(
            1066, 1114, 1117, 1210, 1213, 1216, 1219, 1222, 1225, 1237,
            1255, 1258, 1261, 1264
        ) -> "snowy"

        // 🌨️ Sleet
        in listOf(1069, 1204, 1207, 1249, 1252) -> if (isDay) "rainy" else "night_rainy"

        // ⛈️ Thunder + Rain
        in listOf(1087, 1273, 1276) -> if (isDay) "thunderstorm" else "night_thunderstorm"

        // ❄️⛈️ Thunder + Snow (fallback to snowy or night thunderstorm)
        in listOf(1279, 1282) -> if (isDay) "snowy" else "night_thunderstorm"

        else -> if (isDay) "sunny" else "clear_night" // Fallback
    }
}