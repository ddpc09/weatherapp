package com.floveit.weatherwidget.data.network

import com.floveit.weatherwidget.data.ForecastResponse
import com.floveit.weatherwidget.data.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("v1/current.json")
    suspend fun getCurrentWeather(
        @Query("key") apiKey: String,
        @Query("q") city: String
    ): WeatherResponse

    @GET("v1/forecast.json")
    suspend fun getForecast(
        @Query("key") apiKey: String,
        @Query("q") city: String,
        @Query("days") days: Int,
        @Query("aqi") aqi: String = "no",
        @Query("alerts") alerts: String = "no",
        @Query("hour") hour: String? = null // optional: filter a specific hour
    ): ForecastResponse
}