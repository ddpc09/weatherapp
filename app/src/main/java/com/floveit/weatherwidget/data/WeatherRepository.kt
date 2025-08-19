package com.floveit.weatherwidget.data

interface WeatherRepository {
    suspend fun fetchWeather(city: String): Result<WeatherData>
    suspend fun fetchForecast(city: String, days: Int = 5): Result<Pair<List<DailyForecast>, List<HourlyForecast>>>
}

