package com.floveit.weatherwidget.ui

import com.floveit.weatherwidget.data.DailyForecast
import com.floveit.weatherwidget.data.HourlyForecast

data class WeatherUiState(
    val condition: WeatherCondition = WeatherCondition.Clear,
    val temperature: Int = 0,
    val city: String = "",
    val error: String? = null,
    val conditionCode: Int = 1000,
    val isDay: Boolean = true,
    val feelsLike: Int = 0,
    val humidity: Int = 0,
    val windSpeed: Double = 0.0,
    val daily: List<DailyForecast> = emptyList(),
    val hourly: List<HourlyForecast> = emptyList()
)
