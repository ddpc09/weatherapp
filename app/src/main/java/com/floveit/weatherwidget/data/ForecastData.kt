package com.floveit.weatherwidget.data

data class DailyForecast(
    val date: String,
    val maxTempC: Double,
    val minTempC: Double,
    val conditionCode: Int
)

data class HourlyForecast(
    val time: String,
    val tempC: Double,
    val isDay: Boolean,
    val conditionCode: Int
)