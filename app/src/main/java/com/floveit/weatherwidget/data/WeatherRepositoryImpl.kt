package com.floveit.weatherwidget.data

import com.floveit.weatherwidget.data.network.WeatherApiService

class WeatherRepositoryImpl(
    private val apiService: WeatherApiService,
    private val mapper: WeatherMapper,
    private val apiKey: String,
    private val forecastMapper: ForecastMapper
) : WeatherRepository {

    override suspend fun fetchWeather(city: String): Result<WeatherData> {
        return try {
            val response = apiService.getCurrentWeather(apiKey, city)
            val data = mapper.map(response)
            Result.success(data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    override suspend fun fetchForecast(city: String, days: Int)
            : Result<Pair<List<DailyForecast>, List<HourlyForecast>>> = try {
        val dto = apiService.getForecast(apiKey, city, days.coerceAtLeast(2)) // ⬅️ ensure ≥ 2
        Result.success(
            forecastMapper.mapDaily(dto) to forecastMapper.mapHourly(dto)     // now rolling 24h
        )
    } catch (e: Exception) {
        Result.failure(e)
    }
}