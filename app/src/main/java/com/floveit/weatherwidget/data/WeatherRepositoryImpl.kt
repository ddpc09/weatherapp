package com.floveit.weatherwidget.data

import com.floveit.weatherwidget.data.network.WeatherApiService

class WeatherRepositoryImpl(
    private val apiService: WeatherApiService,
    private val mapper: WeatherMapper,
    private val apiKey: String,
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
}