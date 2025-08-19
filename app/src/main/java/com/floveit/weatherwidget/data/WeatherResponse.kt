package com.floveit.weatherwidget.data

data class WeatherResponse(
    val location: LocationData,
    val current: CurrentWeatherData
)

data class LocationData(
    val name: String,
    val region: String,
    val country: String,
    val tz_id: String? = null,
    val localtime: String? = null
)

//data class CurrentWeatherData(
//    val temp_c: Double,
//    val condition: WeatherConditionData,
//    val is_day: Int
//)

data class CurrentWeatherData(
    val temp_c: Double,
    val condition: WeatherConditionData,
    val is_day: Int,
    val feelslike_c: Double,
    val humidity: Int,
    val wind_kph: Double,
    // val wind_mph: Double,
    // val wind_degree: Int,
    // val wind_dir: String,
    // val pressure_mb: Double,
    // val pressure_in: Double,
    // val precip_mm: Double,
    // val precip_in: Double,
    // val cloud: Int,
    // val windchill_c: Double,
    // val windchill_f: Double,
    // val heatindex_c: Double,
    // val heatindex_f: Double,
    // val dewpoint_c: Double,
    // val dewpoint_f: Double,
    // val vis_km: Double,
    // val vis_miles: Double,
    // val uv: Double,
    // val gust_mph: Double,
    // val gust_kph: Double,
    // val short_rad: Double,
    // val diff_rad: Double,
    // val dni: Double,
    // val gti: Double
)

data class WeatherConditionData(
    val text: String,
    val icon: String,
    val code: Int
)