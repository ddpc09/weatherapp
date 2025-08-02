package com.floveit.weatherwidget.data

data class WeatherResponse(
    val location: LocationData,
    val current: CurrentWeatherData
)

data class LocationData(
    val name: String,
    val region: String,
    val country: String
)

data class CurrentWeatherData(
    val temp_c: Double,
    val condition: WeatherConditionData,
    val is_day: Int
)

data class WeatherConditionData(
    val text: String,
    val icon: String,
    val code: Int
)