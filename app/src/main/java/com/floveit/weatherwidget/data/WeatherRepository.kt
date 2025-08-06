package com.floveit.weatherwidget.data

interface WeatherRepository {
    suspend fun fetchWeather(city: String): Result<WeatherData>
}

