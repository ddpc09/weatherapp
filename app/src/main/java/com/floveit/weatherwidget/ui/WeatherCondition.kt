package com.floveit.weatherwidget.ui

sealed class WeatherCondition {
    object Clear : WeatherCondition()
    object Rain : WeatherCondition()
    object Thunder : WeatherCondition()
    object Cloudy : WeatherCondition()
    object Snow : WeatherCondition()
    // You can add more as needed
}