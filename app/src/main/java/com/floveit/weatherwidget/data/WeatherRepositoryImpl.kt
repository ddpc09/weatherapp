package com.floveit.weatherwidget.data

import com.floveit.weatherwidget.data.location.LocationHelper
import com.floveit.weatherwidget.data.network.WeatherApiService

class WeatherRepositoryImpl(
    private val apiService: WeatherApiService,
    private val mapper: WeatherMapper,
    private val apiKey: String,
    private val locationHelper: LocationHelper
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
    override suspend fun fetchWeatherForCurrentLocation(): Result<WeatherData> {
        return try {
            val city = locationHelper.getCityFromLocation()
                ?: return Result.failure(Exception("Could not detect location"))
            fetchWeather(city)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}