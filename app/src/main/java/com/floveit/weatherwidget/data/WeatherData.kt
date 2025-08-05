package com.floveit.weatherwidget.data

import com.floveit.weatherwidget.ui.WeatherCondition

data class WeatherData(
    val temperature: Double,
    val city: String,
    val condition: WeatherCondition,
    val code: Int,
    val isDay: Boolean,
    val feelsLike: Double,
    val humidity: Int,
    val windSpeed: Double
)